package com.docker.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/soup-api")
@AllArgsConstructor
@Slf4j
@RestController
public class SoapController {

    // SOAP 1.1命名空间（与请求中的soapenv对应）
    private static final String SOAP_NAMESPACE = "http://schemas.xmlsoap.org/soap/envelope/";
    // 业务命名空间（与请求中的her对应）
    private static final String BIZ_NAMESPACE = "http://herenit.com";

    @PostMapping(value = "create-archive",
            consumes = {"text/xml", "application/xml"},
            produces = "text/xml")
    public String createArchive(@RequestBody String soapRequest) {
        log.info("调用SOAP接口成功，参数为:{}", soapRequest);

        try {
            // 解析SOAP XML请求
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // 关键：启用命名空间支持（必须设置为true）
            factory.setNamespaceAware(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            ByteArrayInputStream input = new ByteArrayInputStream(soapRequest.getBytes(StandardCharsets.UTF_8));
            Document document = builder.parse(input);

            // 1. 获取SOAP Body（使用命名空间+标签名）
            // 注意：标签名是"Body"，而非"soapenv:Body"（前缀只是别名，实际用命名空间定位）
            NodeList bodyNodes = document.getElementsByTagNameNS(SOAP_NAMESPACE, "Body");
            if (bodyNodes.getLength() == 0) {
                log.error("SOAP Body元素不存在");
                return createErrorResponse("SOAP消息格式错误，缺少Body元素");
            }
            Element soapBody = (Element) bodyNodes.item(0);

            // 2. 获取业务请求元素her:MessageIn（使用业务命名空间）
            NodeList messageInNodes = soapBody.getElementsByTagNameNS(BIZ_NAMESPACE, "MessageIn");
            if (messageInNodes.getLength() == 0) {
                log.error("SOAP Body中没有找到MessageIn元素");
                return createErrorResponse("SOAP消息格式错误，缺少MessageIn元素");
            }
            Element messageInElement = (Element) messageInNodes.item(0);

            // 3. 获取pInput元素（业务命名空间下）
            NodeList pInputNodes = messageInElement.getElementsByTagNameNS(BIZ_NAMESPACE, "pInput");
            if (pInputNodes.getLength() == 0) {
                log.error("MessageIn中没有找到pInput元素");
                return createErrorResponse("SOAP消息格式错误，缺少pInput元素");
            }
            Element pInputElement = (Element) pInputNodes.item(0);

            // 4. 解析pInput中的HrRequest（注意：HrRequest没有命名空间前缀，使用空命名空间）
            String hrRequestXml = pInputElement.getTextContent().trim();
            log.info("提取到的HrRequest内容: {}", hrRequestXml);

            // 5. 解析HrRequest中的患者信息（嵌套XML解析）
            Map<String, Object> patientInfo = parseHrRequest(hrRequestXml);

            // 后续业务逻辑（同上）
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("patientId", "123445");
            responseMap.put("clinicId", "hl123244");
            responseMap.put("status", "success");
            responseMap.put("message", "档案创建成功");

            // 构造SOAP响应（注意响应也需要正确的命名空间）
            String soapResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                    "<soap:Body>" +
                    "<her:MessageOut xmlns:her=\"http://herenit.com\">" +  // 响应也应使用业务命名空间
                    "<her:patientId>" + responseMap.get("patientId") + "</her:patientId>" +
                    "<her:clinicId>" + responseMap.get("clinicId") + "</her:clinicId>" +
                    "<her:status>" + responseMap.get("status") + "</her:status>" +
                    "<her:message>" + responseMap.get("message") + "</her:message>" +
                    "</her:MessageOut>" +
                    "</soap:Body>" +
                    "</soap:Envelope>";

            return soapResponse;
        } catch (Exception e) {
            log.error("处理SOAP请求失败", e);
            return createErrorResponse(e.getMessage());
        }
    }

    /**
     * 解析pInput中的HrRequest XML
     */
    private Map<String, Object> parseHrRequest(String hrRequestXml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        ByteArrayInputStream input = new ByteArrayInputStream(hrRequestXml.getBytes(StandardCharsets.UTF_8));
        Document document = builder.parse(input);

        // 获取Body节点（HrRequest下的Body，无命名空间）
        Element hrRequestElement = document.getDocumentElement(); // HrRequest根节点
        NodeList bodyNodes = hrRequestElement.getElementsByTagName("Body");
        if (bodyNodes.getLength() == 0) {
            log.error("HrRequest中没有找到Body元素");
            throw new RuntimeException("HrRequest格式错误，缺少Body元素");
        }
        Element bodyElement = (Element) bodyNodes.item(0);

        // 提取患者信息（patName、sex等）
        Map<String, Object> patientInfo = new HashMap<>();
        NodeList childNodes = bodyElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i) instanceof Element) {
                Element element = (Element) childNodes.item(i);
                patientInfo.put(element.getTagName(), element.getTextContent());
            }
        }
        log.info("提取到的患者信息: {}", patientInfo);
        return patientInfo;
    }

    /**
     * 创建SOAP错误响应
     */
    private String createErrorResponse(String message) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body>" +
                "<soap:Fault>" +
                "<faultcode>Client</faultcode>" +
                "<faultstring>" + message + "</faultstring>" +
                "</soap:Fault>" +
                "</soap:Body>" +
                "</soap:Envelope>";
    }


    @PostMapping(value = "create-archive-v2",
            consumes = {"text/xml", "application/xml"},
            produces = "text/xml")
    public String createArchiveV2(@RequestBody String soapRequest) {
        log.info("调用SOAP接口成功，参数为:{}", soapRequest);

        try {
            // 解析SOAP请求（可复用原有逻辑）
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            ByteArrayInputStream input = new ByteArrayInputStream(soapRequest.getBytes(StandardCharsets.UTF_8));
            Document document = builder.parse(input);

            // 验证SOAP Body是否存在
            NodeList bodyNodes = document.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/", "Body");
            if (bodyNodes.getLength() == 0) {
                log.error("SOAP Body元素不存在");
                return createErrorResponse("SOAP消息格式错误，缺少Body元素");
            }
            Element soapBody = (Element) bodyNodes.item(0);

            // 提取业务请求元素（假设是MessageIn）
            NodeList messageInNodes = soapBody.getElementsByTagNameNS("http://herenit.com", "MessageIn");
            if (messageInNodes.getLength() == 0) {
                log.error("SOAP Body中没有找到MessageIn元素");
                return createErrorResponse("SOAP消息格式错误，缺少MessageIn元素");
            }

            // 这里可以添加实际的业务处理逻辑（如提取患者信息等）
            // 为了简化，我们直接返回预设的响应

            // 构造Mock响应（与你提供的XML一致）
            String mockResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                    "<SOAP-ENV:Envelope xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:s='http://www.w3.org/2001/XMLSchema'>\n" +
                    "    <SOAP-ENV:Body>\n" +
                    "        <MessageInResponse xmlns=\"http://herenit.com\">\n" +
                    "            <MessageInResult>\n" +
                    "                <![CDATA[<HrResponse><Head><HipSessionId>7435514</HipSessionId><SystemCode>TJ</SystemCode><TradeCode>PAT202</TradeCode><TradeMessage>成功</TradeMessage><TradeStatus>AA</TradeStatus><TradeTime>2025-09-05 10:03:22</TradeTime></Head><Body><PatientID>7435514</PatientID><VisitNo>1233</VisitNo></Body></HrResponse>]]>\n" +
                    "            </MessageInResult>\n" +
                    "        </MessageInResponse>\n" +
                    "    </SOAP-ENV:Body>\n" +
                    "</SOAP-ENV:Envelope>";

            return mockResponse;
        } catch (Exception e) {
            log.error("处理SOAP请求失败", e);
            return createErrorResponse(e.getMessage());
        }
    }

    @PostMapping(value = "lis_apply",
            consumes = {"text/xml", "application/xml"},
            produces = "text/xml")
    public String lisApply(@RequestBody String soapRequest) {
        log.info("调用SOAP接口lis_apply，参数为:{}", soapRequest);

        try {
            // 构造响应XML（包装在SOAP Envelope中，并将HrResponse放入CDATA）
            String responseXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                    "<SOAP-ENV:Envelope xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:s='http://www.w3.org/2001/XMLSchema'>\n" +
                    "    <SOAP-ENV:Body>\n" +
                    "        <MessageInResponse xmlns=\"http://herenit.com\">\n" +
                    "            <MessageInResult>\n" +
                    "                <![CDATA[<HrResponse><Head><HipSessionId>10467145</HipSessionId><SystemCode>TJ</SystemCode><TradeCode>LAB105</TradeCode><TradeMessage>成功</TradeMessage><TradeStatus>AA</TradeStatus><TradeTime>2025-09-13 12:17:28</TradeTime></Head><Body><BodyList><ExamNo/><LabelNo>2025091300000111</LabelNo><TJApplyNo>45900</TJApplyNo></BodyList><BodyList><ExamNo/><LabelNo>2025091300000112</LabelNo><TJApplyNo>45899</TJApplyNo></BodyList></Body></HrResponse>]]>\n" +
                    "            </MessageInResult>\n" +
                    "        </MessageInResponse>\n" +
                    "    </SOAP-ENV:Body>\n" +
                    "</SOAP-ENV:Envelope>";


            return responseXml;
        } catch (Exception e) {
            log.error("处理SOAP请求失败", e);
            return createErrorResponse(e.getMessage());
        }
    }

    @PostMapping(value = "pacs_apply",
            consumes = {"text/xml", "application/xml"},
            produces = "text/xml")
    public String pacsApply(@RequestBody String soapRequest) {
        log.info("调用SOAP接口lis_apply，参数为:{}", soapRequest);

        try {
            // 构造响应XML（包装在SOAP Envelope中，并将HrResponse放入CDATA）
            String responseXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                    "<SOAP-ENV:Envelope xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:s='http://www.w3.org/2001/XMLSchema'>\n" +
                    "    <SOAP-ENV:Body>\n" +
                    "        <MessageInResponse xmlns=\"http://herenit.com\">\n" +
                    "            <MessageInResult>\n" +
                    "                <![CDATA[<HrResponse><Head><HipSessionId>10467145</HipSessionId><SystemCode>TJ</SystemCode><TradeCode>LAB105</TradeCode><TradeMessage>成功</TradeMessage><TradeStatus>AA</TradeStatus><TradeTime>2025-09-13 12:17:28</TradeTime></Head><Body><BodyList><ExamNo>2025091300000133</ExamNo><LabelNo/><TJApplyNo>45900</TJApplyNo></BodyList><BodyList><ExamNo>2025091300000144</ExamNo><LabelNo/><TJApplyNo>45900</TJApplyNo></BodyList></Body></HrResponse>]]>\n" +
                    "            </MessageInResult>\n" +
                    "        </MessageInResponse>\n" +
                    "    </SOAP-ENV:Body>\n" +
                    "</SOAP-ENV:Envelope>";


            return responseXml;
        } catch (Exception e) {
            log.error("处理SOAP请求失败", e);
            return createErrorResponse(e.getMessage());
        }
    }

}
