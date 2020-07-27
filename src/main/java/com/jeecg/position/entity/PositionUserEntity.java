package com.jeecg.position.entity;

import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;

/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Entity
 * @Description: 人员管理
 * @date 2019-09-20 15:39:01
 */
@Entity
@Table(name = "position_user", schema = "")
@SuppressWarnings("serial")
public class PositionUserEntity implements java.io.Serializable {
    /**
     * 主键
     */
    private java.lang.String id;
    /**
     * 卡号
     */
    @Excel(name = "卡号", width = 15)
    private java.lang.String imei;
    /**
     * 姓名
     */
    @Excel(name = "姓名", width = 15)
    private java.lang.String name;
    /**
     * 所属单位
     */
    @Excel(name = "所属单位", width = 15, dictTable = "position_user_org", dicCode = "id", dicText = "name")
    private java.lang.String company;
    /**
     * 使用对象
     */
    @Excel(name = "使用对象", width = 15, dicCode = "pUseObj")
    private java.lang.Integer useObject;
    /**
     * 电话
     */
    @Excel(name = "电话", width = 15)
    private java.lang.String telephone;
    /**
     * 性别
     */
    @Excel(name = "性别", width = 15, dicCode = "sex")
    private java.lang.Integer sex;
    /**
     * 年龄
     */
    @Excel(name = "年龄", width = 15)
    private java.lang.Integer age;
    /**
     * 状态
     */
    @Excel(name = "状态", width = 15, dicCode = "isValid")
    private java.lang.Integer status;
    /**
     * 创建日期
     */
    private java.util.Date createDate;
    /**
     * 更新日期
     */
    private java.util.Date updateDate;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    private java.lang.String remark;

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  主键
     */
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")

    @Column(name = "ID", nullable = false, length = 36)
    public java.lang.String getId() {
        return this.id;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  主键
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  卡号
     */

    @Column(name = "IMEI", nullable = false, length = 36)
    public java.lang.String getImei() {
        return this.imei;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  卡号
     */
    public void setImei(java.lang.String imei) {
        this.imei = imei;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  姓名
     */

    @Column(name = "NAME", nullable = false, length = 32)
    public java.lang.String getName() {
        return this.name;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  姓名
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  所属单位
     */

    @Column(name = "COMPANY", nullable = false, length = 50)
    public java.lang.String getCompany() {
        return this.company;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  所属单位
     */
    public void setCompany(java.lang.String company) {
        this.company = company;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  使用对象
     */

    @Column(name = "USE_OBJECT", nullable = false, length = 1)
    public java.lang.Integer getUseObject() {
        return this.useObject;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  使用对象
     */
    public void setUseObject(java.lang.Integer useObject) {
        this.useObject = useObject;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  电话
     */

    @Column(name = "TELEPHONE", nullable = true, length = 15)
    public java.lang.String getTelephone() {
        return this.telephone;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  电话
     */
    public void setTelephone(java.lang.String telephone) {
        this.telephone = telephone;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  性别
     */

    @Column(name = "SEX", nullable = true, length = 1)
    public java.lang.Integer getSex() {
        return this.sex;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  性别
     */
    public void setSex(java.lang.Integer sex) {
        this.sex = sex;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  年龄
     */

    @Column(name = "AGE", nullable = true, length = 2)
    public java.lang.Integer getAge() {
        return this.age;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  状态
     */
    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  状态
     */

    @Column(name = "STATUS", nullable = false, length = 1)
    public java.lang.Integer getStatus() {
        return this.status;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  年龄
     */
    public void setAge(java.lang.Integer age) {
        this.age = age;
    }

    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  创建日期
     */

    @Column(name = "CREATE_DATE", nullable = true, length = 20)
    public java.util.Date getCreateDate() {
        return this.createDate;
    }

    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  创建日期
     */
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  更新日期
     */

    @Column(name = "UPDATE_DATE", nullable = true, length = 20)
    public java.util.Date getUpdateDate() {
        return this.updateDate;
    }

    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  更新日期
     */
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  备注
     */

    @Column(name = "REMARK", nullable = true, length = 80)
    public java.lang.String getRemark() {
        return this.remark;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  备注
     */
    public void setRemark(java.lang.String remark) {
        this.remark = remark;
    }
}