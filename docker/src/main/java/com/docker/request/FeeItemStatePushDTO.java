package com.docker.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "FeeItemStatePushDTO",description = "收费项状态推送参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeeItemStatePushDTO implements Serializable {

    private static final long serialVersionUID = 6180153038388834722L;

    @ApiModelProperty(value = "厂商类型")
    private String serviceProviderType;

    @ApiModelProperty(value = "体检人编号")
    private String patientCode;

    @ApiModelProperty(value = "收费项外部编码")
    private String outFeeItemId;

    @ApiModelProperty(value = "申请单号")
    private String applyNo;

    @ApiModelProperty(value = "条码号")
    private String barCode;

    @ApiModelProperty(value = "以哪个字段为准 1:outFeeItemId 2:applyNo  3:barCode")
    private Integer paramType;

    @ApiModelProperty(value = "0:已核收（即：已上机）1:已总检（即：已出报告）2:取消核收（即：取消上机） 99：特殊状态（99 不需要修改is_liststate）")
    private Integer state;

    @ApiModelProperty(value = "状态名称（vpfi 表中新增字段保存状态名称）")
    private String stateName;

    @ApiModelProperty(value = "检查医生")
    private String examDoctor;

    @ApiModelProperty(value = "检查时间  格式：yyyy-MM-dd HH:mm:ss")
    private String examTime;

    @ApiModelProperty(value = "操作医生")
    private String operateDoctor;

    @ApiModelProperty(value = "操作时间  格式：yyyy-MM-dd HH:mm:ss")
    private String operateTime;
}
