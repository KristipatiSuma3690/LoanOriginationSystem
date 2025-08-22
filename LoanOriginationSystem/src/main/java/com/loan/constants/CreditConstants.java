package com.loan.constants;

public class CreditConstants {

    public static final String DATE_FORMAT_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String DEFAULT="default";

    public static final String OPERATION_NAME = "updateAvailableCredit";
    public static final String AUDIT_CLASS = "OE-Transform-CreditService";
    public static final String AUDIT_SERVICE_TYPE = "OE_TRANSFORM-SERVICES";

    public static final String EVENT_SOURCE = "BW_ESB";
    public static final String AUDIT_SOURCE = "BW COM SVCS";
    public static final String AUDIT_VERSION = "v1";
    public static final String AUDIT_USER_ID = "oesvcusr";

    public static final String SUCCESS_MESSAGE = "COMPLETED:SUCCESS";

    public static final String REMOVED_NAMESPACE_PREFIX = "SOAP-ENV";
    public static final String ADD_PREFIX = "soapenv";

    public static final String ERROR="ERROR";
    public static final String XML_ENTITY_CHECK = "http://apache.org/xml/features/disallow-doctype-decl";
    public static final String XML_EXTERNAL_ENTITY_CHECK = "http://xml.org/sax/features/external-general-entities";

    public static final String STRING_TO_DOC_EXCEPTION_MSG = "Exception while generating xml document from the xml string : {}";
    public static final String ACTION_NAME = "soapAction";
    public static final String AUDIT_NAMESPACE_DECLARATION_URI = "http://level3/common/services/audit/v1";
    public static final String JMS_EXCEPTION_MSG = "Exception thrown while publishing data to JMS ::";
    public static final String JMS_SUCCESS_MESSAGE = "Message Sent Successfully";
    public static final String DOC_TO_STRING_EXCEPTION_MSG = "Exception while generating string from xml document: {}";
    public static final String EXCEPTION_MSG = "Exception while calling external CustomerAccount service : {}";
    public static final String TO_XML_EXCEPTION_MSG = "Exception while generating xml from the object : {}";

    public static final String JMS_URL = "tcp://tibems01.env1.idc1.level3.com:7222,tcp://tibems02.env1.idc1.level3.com:7222";
    public static final String JMS_QUEUE = "LVLT.Common.CommonServices.Audit.AuditNotification.V1_0.NTF.Q";
    public static final String JMS_USERNAME = "EMS_User";

    public static final String JMS_PASSWORD = "EMS_User";

}
