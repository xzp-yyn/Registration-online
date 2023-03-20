package com.xzp.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzp.Enum.MqConstant;
import com.xzp.enums.OrderStatusEnum;
import com.xzp.hosp.HospClient;
import com.xzp.mapper.OrderMapper;
import com.xzp.model.hosp.HospitalSet;
import com.xzp.model.hosp.Hosptial;
import com.xzp.model.hosp.Schedule;
import com.xzp.model.order.Order;
import com.xzp.model.user.Patient;
import com.xzp.result.Result;
import com.xzp.result.ResultCode;
import com.xzp.service.OrderService;
import com.xzp.service.RabbitService;
import com.xzp.user.PatientClient;
import com.xzp.util.HttpRequestHelper;
import com.xzp.util.Time;
import com.xzp.util.TokenHelper;
import com.xzp.util.YyghException;
import com.xzp.vo.hosp.ScheduleMqVo;
import com.xzp.vo.order.OrderQueryVo;
import com.xzp.vo.order.DataShowVo;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * 时间未到，资格未够，继续努力！
 * @Author xuezhanpeng
 * @Date 2022/11/16 9:52
 * @Version 1.0
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private ReentrantReadWriteLock readWriteLock=new ReentrantReadWriteLock();
    {

    }

    @Autowired
    private HospClient hospClient;

    @Autowired
    private PatientClient patientClient;

    @Autowired
    private RabbitService rabbitService;


    @Value("${hosp.requestUrl.baseUrl}")
    public void setRequestUrl(String requestUrl) {
        OrderServiceImpl.requestUrl = requestUrl;
    }

    private static String requestUrl;

    private static  String saveOrderUrl;

    private static String updateCancelStatusUrl;

    @Value("${hosp.requestUrl.updateCancelStatusUrl}")
    public  void setUpdateCancelStatusUrl(String updateCancelStatusUrl) {
        OrderServiceImpl.updateCancelStatusUrl = updateCancelStatusUrl;
    }

    @Value("${hosp.requestUrl.submitOrderUrl}")
    public void setSaveOrderUrl(String saveOrderUrl) {
        OrderServiceImpl.saveOrderUrl = saveOrderUrl;
    }

    /**
     * 1.得到排班和就诊人信息
     * 2.请求医院的下单接口，生成订单，返回信息
     * 3.封装信息，保存到数据库
     * @param patientid
     * @param scheduleid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public  Long saveOrder(String patientid, String scheduleid) {

        Object patientobj = patientClient.findByid(patientid).getData();
        Object scheduleobj = hospClient.selectScheduleById(String.valueOf(scheduleid)).getData();

        Patient patient1 = jsonToObject(patientobj,Patient.class);
        Schedule schedule= jsonToObject(scheduleobj,Schedule.class);
        Object hospitalobj = hospClient.getMongoHospByhosCode(schedule.getHoscode()).getData();
        Object hospitalsetobj = hospClient.getHosByHoscode(schedule.getHoscode()).getData();
        HospitalSet hospitalSet = jsonToObject(hospitalsetobj, HospitalSet.class);
        Hosptial hosptial=jsonToObject(hospitalobj,Hosptial.class);
        String signKey = hospitalSet.getSignKey();
        // 判断是否超过预约截止时间或者在预约时间之前
        Date startTime = Time.changeToDate(new Date(), hosptial.getBookingRule().getReleaseTime()).toDate();
        Date endTime = Time.changeToDate(schedule.getWorkDate(), hosptial.getBookingRule().getStopTime()).toDate();
        if(new DateTime(startTime).isAfterNow() || new DateTime(endTime).isBeforeNow()){
            throw new YyghException(ResultCode.TIME_NO);
        }

        Order order = new Order();
        BeanUtils.copyProperties(schedule,order);
        order.setUserId(patient1.getUserId());
        String traderNo =System.currentTimeMillis()+UUID.randomUUID().toString();
        order.setOutTradeNo(traderNo);
        order.setHosname(hospitalSet.getHosname());
        order.setDepname((String) schedule.getParam().get("depname"));
        order.setScheduleId(scheduleid);
        order.setPatientId(patient1.getId());
        order.setPatientName(patient1.getName());
        order.setPatientPhone(patient1.getPhone());
        order.setAmount(schedule.getAmount());
        DateTime quitTime = Time.changeToDate(schedule.getWorkDate(), hosptial.getBookingRule().getQuitTime());
        order.setQuitTime(quitTime.toDate());
        order.setOrderStatus(OrderStatusEnum.UNPAID.getStatus());
        baseMapper.insert(order);

        Map<String,Object> requestmap= new HashMap<>();
//        封装医院排班信息
        requestmap.put("hoscode",schedule.getHoscode());
        requestmap.put("depcode",schedule.getDepcode());
        requestmap.put("hosScheduleId",schedule.getHosScheduleId());
        requestmap.put("reserveTime",schedule.getWorkTime());
        requestmap.put("reserveDate",new DateTime(schedule.getWorkDate()).toString("yyyy-MM-dd"));
        requestmap.put("amount",schedule.getAmount());
//        医院接口也会封装就诊人信息，并转换为对象
        this.packagePatient(requestmap,patient1);
        //接口会对加密后的签名校验，sign即SignKey加密后的签名
        String sign = HttpRequestHelper.getSign(requestmap, signKey);
        requestmap.put("sign",sign);

        JSONObject jsonObject = HttpRequestHelper.sendRequest(requestmap, requestUrl + saveOrderUrl);
        if(jsonObject.getInteger("code")==200){
//            获取返回结果转换为Map集合
            Map<String,Object> map = jsonToObject(jsonObject.get("data"), Map.class);
            String hosRecordId= String.valueOf(map.get("hosRecordId"));
            Integer number= (Integer) map.get("number");
            String fetchTime = String.valueOf(map.get("fetchTime"));
            String fetchAddress = String.valueOf(map.get("fetchAddress"));
            order.setHosRecordId(hosRecordId);
            order.setNumber(number);
            String dateTime = new DateTime(schedule.getWorkDate()).toString("yyyy-MM-dd");
            Date date = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(dateTime).toDate();
            order.setReserveDate(date);
            order.setReserveTime(schedule.getWorkTime());
            order.setFetchTime(fetchTime);
            order.setFetchAddress(fetchAddress);
            baseMapper.updateById(order);
            Integer reservedNumber = (Integer) map.get("reservedNumber");
            Integer availableNumber=(Integer) map.get("availableNumber");
            //发送mq消息，更新预约数
            synchronized (order.getId()){
                ScheduleMqVo scheduleMqVo = new ScheduleMqVo();
                scheduleMqVo.setScheduleId(scheduleid);
                scheduleMqVo.setAvailableNumber(availableNumber);
                scheduleMqVo.setReservedNumber(reservedNumber);
                rabbitService.sendMqMessage(MqConstant.EXCHANGE_DIRECT_ORDER,MqConstant.ROUTING_ORDER,scheduleMqVo);
            }

        }else {
            throw new YyghException(ResultCode.SERVICE_ERROR);
        }
        return order.getId();
    }
    @Override
    public Order getOrderByid(Long id) {
        Order order = this.getById(id);
        if (order==null){
            throw new YyghException(ResultCode.DATA_ERROR);
        }
        order.getParam().put("orderStatus",OrderStatusEnum.getStatusNameByStatus(order.getOrderStatus()));
        Result result = patientClient.findByid(String.valueOf(order.getPatientId()));
        if(result.getCode()==200){
            Object data = result.getData();
            Patient patient = jsonToObject(data, Patient.class);
            if(patient!=null){
                order.getParam().put("patientNumber",patient.getCertificatesNo());
            }
        }
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelStatus(Long id) {
        Order order = baseMapper.selectById(id);
        if(order==null){
            throw new YyghException(ResultCode.CANCEL_ORDER_FAIL);
        }
        if(order.getOrderStatus().equals(OrderStatusEnum.CANCLE.getStatus())){
            return;
        }
        order.setOrderStatus(OrderStatusEnum.CANCLE.getStatus());
        baseMapper.updateById(order);
        Map<String,Object> requestmap= new HashMap<>();
        Object hospitalsetobj = hospClient.getHosByHoscode(order.getHoscode()).getData();
        HospitalSet hospitalSet = jsonToObject(hospitalsetobj, HospitalSet.class);
        String signKey = hospitalSet.getSignKey();
        requestmap.put("sign",signKey);
        requestmap.put("hoscode",order.getHoscode());
        requestmap.put("hosRecordId",order.getHosRecordId());
        JSONObject jsonObject = HttpRequestHelper.sendRequest(requestmap, requestUrl + updateCancelStatusUrl);
        if(jsonObject.getInteger("code")==200){
            //通过远程调用进行更新
            try {
                readWriteLock.writeLock().lock();
                Map<String,Object> scheduleMap= new HashMap<>();
                Object scheduledata = hospClient.selectScheduleById(order.getScheduleId()).getData();
                Map<String,Object> schemap = JSONObject.parseObject(JSONObject.toJSONString(scheduledata), Map.class);
                Integer availableNumber= (Integer) schemap.get("availableNumber");
                schemap.replace("availableNumber",availableNumber+1);
                scheduleMap.put("schedule",schemap);
                Integer code = hospClient.updateScheduleById(scheduleMap).getCode();
                if(code!=200){
                    throw new YyghException(ResultCode.CANCEL_ORDER_FAIL);
                }
            }catch (Exception e){
                throw new YyghException(ResultCode.CANCEL_ORDER_FAIL);
            }finally {
                readWriteLock.writeLock().unlock();
            }


            //通过mq进行更新
//            ScheduleMqVo scheduleMqVo = new ScheduleMqVo();
//            scheduleMqVo.setScheduleId(order.getScheduleId());
//            rabbitService.sendMqMessage(MqConstant.EXCHANGE_DIRECT_ORDER,MqConstant.ROUTING_CANCEL,scheduleMqVo);
        }

    }

    @Override
    public IPage<Order> findall(Page<Order> page1, OrderQueryVo queryVo) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if(queryVo.getParam()!=null &&!StringUtils.isEmpty(queryVo.getParam().get("token"))){
            String token= (String) queryVo.getParam().get("token");
            Long userId = TokenHelper.getUserId(token);
            wrapper.eq(Order::getUserId,userId);
        }
        wrapper.eq(!StringUtils.isEmpty(queryVo.getOrderStatus()),Order::getOrderStatus,queryVo.getOrderStatus());
        wrapper.like(!StringUtils.isEmpty(queryVo.getPatientName()),Order::getPatientName,queryVo.getPatientName());
        wrapper.eq(!StringUtils.isEmpty(queryVo.getOutTradeNo()),Order::getOutTradeNo,queryVo.getOutTradeNo());
        wrapper.eq(!StringUtils.isEmpty(queryVo.getPatientId()),Order::getPatientId,queryVo.getPatientId());
        wrapper.ge(!StringUtils.isEmpty(queryVo.getStartTime()),Order::getCreateTime,queryVo.getStartTime());
        wrapper.le(!StringUtils.isEmpty(queryVo.getEndTime()),Order::getCreateTime,queryVo.getEndTime());
        Page<Order> page = this.page(page1, wrapper);
        page.getRecords().stream().forEach(item->{
            item.getParam().put("orderStatus",OrderStatusEnum.getStatusNameByStatus(item.getOrderStatus()));
        });
        return page;
    }

    private void packagePatient(Map<String, Object> requestmap, Patient patient1) {
        requestmap.put("id",patient1.getId());
        requestmap.put("createTime",new DateTime(patient1.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"));
        requestmap.put("updateTime",new DateTime(patient1.getUpdateTime()).toString("yyyy-MM-dd HH:mm:ss"));
        requestmap.put("isDeleted",patient1.getIsDeleted());
        requestmap.put("userId",patient1.getUserId());
        requestmap.put("name",patient1.getName());
        requestmap.put("certificatesType",patient1.getCertificatesType());
        requestmap.put("certificatesNo",patient1.getCertificatesNo());
        requestmap.put("sex",patient1.getSex());
        requestmap.put("birthdate",new DateTime(patient1.getBirthdate()).toString("yyyy-MM-dd HH:mm:ss"));
        requestmap.put("phone",patient1.getPhone());
        requestmap.put("isMarry",patient1.getIsMarry());
        requestmap.put("provinceCode",patient1.getProvinceCode());
        requestmap.put("cityCode",patient1.getCityCode());
        requestmap.put("districtCode",patient1.getDistrictCode());
        requestmap.put("address",patient1.getAddress());
        requestmap.put("contactsName",patient1.getContactsName());
        requestmap.put("contactsCertificatesType",patient1.getContactsCertificatesType());
        requestmap.put("contactsCertificatesNo",patient1.getContactsCertificatesNo());
        requestmap.put("contactsPhone",patient1.getContactsPhone());
    }
    protected  <T> T jsonToObject(Object object,Class<T> clazz){
        String toJSONString = JSONObject.toJSONString(object);
        T t1 = JSONObject.parseObject(toJSONString, clazz);
        return t1;
    }
    @Override
    public Map<String, Object> selectOrderList(DataShowVo dataShowVo) {
        Map<String, Object>  result=new HashMap<>();
        List<DataShowVo> all = baseMapper.findAllByParam(dataShowVo);
        List<Integer> integers = all.stream().map(DataShowVo::getCount).collect(Collectors.toList());
        List<String> dates = all.stream().map((item)->{
            String s = new DateTime(item.getReserveDate()).toString("yyyy-MM-dd");
            return s;
        }).collect(Collectors.toList());
        result.put("counts",integers);
        result.put("dates",dates);
        return result;
    }
}
