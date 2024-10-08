package cn.iocoder.yudao.module.system.dal.mysql.sms;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.system.controller.admin.sms.vo.log.SmsLogPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.sms.SmsLogDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface SmsLogMapper extends BaseMapperX<SmsLogDO> {

    default PageResult<SmsLogDO> selectPage(SmsLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SmsLogDO>()
                .eqIfPresent(SmsLogDO::getChannelId, reqVO.getChannelId())
                .eqIfPresent(SmsLogDO::getTemplateId, reqVO.getTemplateId())
                .likeIfPresent(SmsLogDO::getMobile, reqVO.getMobile())
                .eqIfPresent(SmsLogDO::getSendStatus, reqVO.getSendStatus())
                .betweenIfPresent(SmsLogDO::getSendTime, reqVO.getSendTime())
                .eqIfPresent(SmsLogDO::getReceiveStatus, reqVO.getReceiveStatus())
                .betweenIfPresent(SmsLogDO::getReceiveTime, reqVO.getReceiveTime())
                .orderByDesc(SmsLogDO::getId));
    }

    /**
     * irujia修改
     * 根据短信 API 返回的序列号 (api_serial_no) 查找对应的日志 ID (logId)
     *
     * @param apiSerialNo 短信的 API 序列号
     * @return 日志 ID，如果没有找到返回 null
     */
    default Long findLogIdByApiSerialNo(String apiSerialNo) {
        return Optional.ofNullable(selectOne(new LambdaQueryWrapperX<SmsLogDO>()
                        .select(SmsLogDO::getId) // 只选择 ID 字段
                        .eq(SmsLogDO::getApiSerialNo, apiSerialNo))) // 使用 api_serial_no 字段进行查询
                .map(SmsLogDO::getId)
                .orElse(null);
    }
}
