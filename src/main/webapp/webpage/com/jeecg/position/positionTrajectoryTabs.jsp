<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,DatePicker,autocomplete"></t:base>
<div class="easyui-layout" fit="true">
    <div region="center" style="padding:0px;border:0px">
        <t:tabs id="tabsOne" iframe="true" tabPosition="top" fit="true">
            <t:tab href="positionTrajectoryController.do?list" iframe="true" icon="icon-search" title="定位列表" id="list"></t:tab>
            <t:tab href="positionTrajectoryController.do?maps" iframe="true" icon="icon-save" title="定位轨迹" id="maps"></t:tab>
        </t:tabs>
    </div>
</div>