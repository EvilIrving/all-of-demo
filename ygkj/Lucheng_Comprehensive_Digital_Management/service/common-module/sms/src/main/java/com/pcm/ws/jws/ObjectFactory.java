
package com.pcm.ws.jws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.pcm.ws.jws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AddFileResponse_QNAME = new QName("http://v1.ws.api.service.yxt.gooben.com/", "addFileResponse");
    private final static QName _GetSentResultCall_QNAME = new QName("http://v1.ws.api.service.yxt.gooben.com/", "getSentResultCall");
    private final static QName _AddFile_QNAME = new QName("http://v1.ws.api.service.yxt.gooben.com/", "addFile");
    private final static QName _GetSentTaskResultCallResponse_QNAME = new QName("http://v1.ws.api.service.yxt.gooben.com/", "getSentTaskResultCallResponse");
    private final static QName _SubmitTaskResponse_QNAME = new QName("http://v1.ws.api.service.yxt.gooben.com/", "submitTaskResponse");
    private final static QName _GetSentTaskResultSms_QNAME = new QName("http://v1.ws.api.service.yxt.gooben.com/", "getSentTaskResultSms");
    private final static QName _GetSentResultSms_QNAME = new QName("http://v1.ws.api.service.yxt.gooben.com/", "getSentResultSms");
    private final static QName _Authorization_QNAME = new QName("http://v1.ws.api.service.yxt.gooben.com/", "authorization");
    private final static QName _GetSubmitResult_QNAME = new QName("http://v1.ws.api.service.yxt.gooben.com/", "getSubmitResult");
    private final static QName _GetSentResultCallResponse_QNAME = new QName("http://v1.ws.api.service.yxt.gooben.com/", "getSentResultCallResponse");
    private final static QName _GetSentTaskResultSmsResponse_QNAME = new QName("http://v1.ws.api.service.yxt.gooben.com/", "getSentTaskResultSmsResponse");
    private final static QName _Token_QNAME = new QName("http://v1.ws.api.service.yxt.gooben.com/", "token");
    private final static QName _GetSentTaskResultCall_QNAME = new QName("http://v1.ws.api.service.yxt.gooben.com/", "getSentTaskResultCall");
    private final static QName _AuthorizationResponse_QNAME = new QName("http://v1.ws.api.service.yxt.gooben.com/", "authorizationResponse");
    private final static QName _SubmitTask_QNAME = new QName("http://v1.ws.api.service.yxt.gooben.com/", "submitTask");
    private final static QName _GetSubmitResultResponse_QNAME = new QName("http://v1.ws.api.service.yxt.gooben.com/", "getSubmitResultResponse");
    private final static QName _GetSentResultSmsResponse_QNAME = new QName("http://v1.ws.api.service.yxt.gooben.com/", "getSentResultSmsResponse");
    private final static QName _GetSmsReceive_QNAME = new QName("http://v1.ws.api.service.yxt.gooben.com/", "getSmsReceive");
    private final static QName _GetSmsReceiveResponse_QNAME = new QName("http://v1.ws.api.service.yxt.gooben.com/", "getSmsReceiveResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.pcm.ws.jws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ItfOptions }
     * 
     */
    public ItfOptions createItfOptions() {
        return new ItfOptions();
    }

    /**
     * Create an instance of {@link ItfOptions.ItfExtentOptions }
     * 
     */
    public ItfOptions.ItfExtentOptions createItfOptionsItfExtentOptions() {
        return new ItfOptions.ItfExtentOptions();
    }

    /**
     * Create an instance of {@link GetSentResultSmsResponse }
     * 
     */
    public GetSentResultSmsResponse createGetSentResultSmsResponse() {
        return new GetSentResultSmsResponse();
    }

    /**
     * Create an instance of {@link GetSmsReceive }
     * 
     */
    public GetSmsReceive createGetSmsReceive() {
        return new GetSmsReceive();
    }

    /**
     * Create an instance of {@link GetSmsReceiveResponse }
     * 
     */
    public GetSmsReceiveResponse createGetSmsReceiveResponse() {
        return new GetSmsReceiveResponse();
    }

    /**
     * Create an instance of {@link GetSubmitResultResponse }
     * 
     */
    public GetSubmitResultResponse createGetSubmitResultResponse() {
        return new GetSubmitResultResponse();
    }

    /**
     * Create an instance of {@link GetSentTaskResultCall }
     * 
     */
    public GetSentTaskResultCall createGetSentTaskResultCall() {
        return new GetSentTaskResultCall();
    }

    /**
     * Create an instance of {@link AuthorizationResponse }
     * 
     */
    public AuthorizationResponse createAuthorizationResponse() {
        return new AuthorizationResponse();
    }

    /**
     * Create an instance of {@link SubmitTask }
     * 
     */
    public SubmitTask createSubmitTask() {
        return new SubmitTask();
    }

    /**
     * Create an instance of {@link GetSubmitResult }
     * 
     */
    public GetSubmitResult createGetSubmitResult() {
        return new GetSubmitResult();
    }

    /**
     * Create an instance of {@link GetSentResultCallResponse }
     * 
     */
    public GetSentResultCallResponse createGetSentResultCallResponse() {
        return new GetSentResultCallResponse();
    }

    /**
     * Create an instance of {@link GetSentTaskResultSmsResponse }
     * 
     */
    public GetSentTaskResultSmsResponse createGetSentTaskResultSmsResponse() {
        return new GetSentTaskResultSmsResponse();
    }

    /**
     * Create an instance of {@link Authorization }
     * 
     */
    public Authorization createAuthorization() {
        return new Authorization();
    }

    /**
     * Create an instance of {@link GetSentResultSms }
     * 
     */
    public GetSentResultSms createGetSentResultSms() {
        return new GetSentResultSms();
    }

    /**
     * Create an instance of {@link GetSentTaskResultSms }
     * 
     */
    public GetSentTaskResultSms createGetSentTaskResultSms() {
        return new GetSentTaskResultSms();
    }

    /**
     * Create an instance of {@link AddFileResponse }
     * 
     */
    public AddFileResponse createAddFileResponse() {
        return new AddFileResponse();
    }

    /**
     * Create an instance of {@link GetSentResultCall }
     * 
     */
    public GetSentResultCall createGetSentResultCall() {
        return new GetSentResultCall();
    }

    /**
     * Create an instance of {@link AddFile }
     * 
     */
    public AddFile createAddFile() {
        return new AddFile();
    }

    /**
     * Create an instance of {@link GetSentTaskResultCallResponse }
     * 
     */
    public GetSentTaskResultCallResponse createGetSentTaskResultCallResponse() {
        return new GetSentTaskResultCallResponse();
    }

    /**
     * Create an instance of {@link SubmitTaskResponse }
     * 
     */
    public SubmitTaskResponse createSubmitTaskResponse() {
        return new SubmitTaskResponse();
    }

    /**
     * Create an instance of {@link ArrayOfItfTaskSmsListResult }
     * 
     */
    public ArrayOfItfTaskSmsListResult createArrayOfItfTaskSmsListResult() {
        return new ArrayOfItfTaskSmsListResult();
    }

    /**
     * Create an instance of {@link ItfAcceptedDetail }
     * 
     */
    public ItfAcceptedDetail createItfAcceptedDetail() {
        return new ItfAcceptedDetail();
    }

    /**
     * Create an instance of {@link ItfSentResultCallWs }
     * 
     */
    public ItfSentResultCallWs createItfSentResultCallWs() {
        return new ItfSentResultCallWs();
    }

    /**
     * Create an instance of {@link ArrayOfItfAcceptedDetail }
     * 
     */
    public ArrayOfItfAcceptedDetail createArrayOfItfAcceptedDetail() {
        return new ArrayOfItfAcceptedDetail();
    }

    /**
     * Create an instance of {@link ArrayOfItfSentResultSmsWs }
     * 
     */
    public ArrayOfItfSentResultSmsWs createArrayOfItfSentResultSmsWs() {
        return new ArrayOfItfSentResultSmsWs();
    }

    /**
     * Create an instance of {@link ArrayOfItfBeFilteredNumber }
     * 
     */
    public ArrayOfItfBeFilteredNumber createArrayOfItfBeFilteredNumber() {
        return new ArrayOfItfBeFilteredNumber();
    }

    /**
     * Create an instance of {@link TokenObj }
     * 
     */
    public TokenObj createTokenObj() {
        return new TokenObj();
    }

    /**
     * Create an instance of {@link InterfaceResult }
     * 
     */
    public InterfaceResult createInterfaceResult() {
        return new InterfaceResult();
    }

    /**
     * Create an instance of {@link ItfTaskSmsListResult }
     * 
     */
    public ItfTaskSmsListResult createItfTaskSmsListResult() {
        return new ItfTaskSmsListResult();
    }

    /**
     * Create an instance of {@link ItfSubmitResultWs }
     * 
     */
    public ItfSubmitResultWs createItfSubmitResultWs() {
        return new ItfSubmitResultWs();
    }

    /**
     * Create an instance of {@link ItfBeFilteredNumber }
     * 
     */
    public ItfBeFilteredNumber createItfBeFilteredNumber() {
        return new ItfBeFilteredNumber();
    }

    /**
     * Create an instance of {@link ItfSendDetail }
     * 
     */
    public ItfSendDetail createItfSendDetail() {
        return new ItfSendDetail();
    }

    /**
     * Create an instance of {@link ItfSentResultSmsWs }
     * 
     */
    public ItfSentResultSmsWs createItfSentResultSmsWs() {
        return new ItfSentResultSmsWs();
    }

    /**
     * Create an instance of {@link ItfSmsReceive }
     * 
     */
    public ItfSmsReceive createItfSmsReceive() {
        return new ItfSmsReceive();
    }

    /**
     * Create an instance of {@link ArrayOfItfSentResultCallWs }
     * 
     */
    public ArrayOfItfSentResultCallWs createArrayOfItfSentResultCallWs() {
        return new ArrayOfItfSentResultCallWs();
    }

    /**
     * Create an instance of {@link ItfSmsOption }
     * 
     */
    public ItfSmsOption createItfSmsOption() {
        return new ItfSmsOption();
    }

    /**
     * Create an instance of {@link ArrayOfItfSmsReceiveWs }
     * 
     */
    public ArrayOfItfSmsReceiveWs createArrayOfItfSmsReceiveWs() {
        return new ArrayOfItfSmsReceiveWs();
    }

    /**
     * Create an instance of {@link ItfCallOption }
     * 
     */
    public ItfCallOption createItfCallOption() {
        return new ItfCallOption();
    }

    /**
     * Create an instance of {@link ItfOptions.ItfExtentOptions.Entry }
     * 
     */
    public ItfOptions.ItfExtentOptions.Entry createItfOptionsItfExtentOptionsEntry() {
        return new ItfOptions.ItfExtentOptions.Entry();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v1.ws.api.service.yxt.gooben.com/", name = "addFileResponse")
    public JAXBElement<AddFileResponse> createAddFileResponse(AddFileResponse value) {
        return new JAXBElement<AddFileResponse>(_AddFileResponse_QNAME, AddFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSentResultCall }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v1.ws.api.service.yxt.gooben.com/", name = "getSentResultCall")
    public JAXBElement<GetSentResultCall> createGetSentResultCall(GetSentResultCall value) {
        return new JAXBElement<GetSentResultCall>(_GetSentResultCall_QNAME, GetSentResultCall.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v1.ws.api.service.yxt.gooben.com/", name = "addFile")
    public JAXBElement<AddFile> createAddFile(AddFile value) {
        return new JAXBElement<AddFile>(_AddFile_QNAME, AddFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSentTaskResultCallResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v1.ws.api.service.yxt.gooben.com/", name = "getSentTaskResultCallResponse")
    public JAXBElement<GetSentTaskResultCallResponse> createGetSentTaskResultCallResponse(GetSentTaskResultCallResponse value) {
        return new JAXBElement<GetSentTaskResultCallResponse>(_GetSentTaskResultCallResponse_QNAME, GetSentTaskResultCallResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubmitTaskResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v1.ws.api.service.yxt.gooben.com/", name = "submitTaskResponse")
    public JAXBElement<SubmitTaskResponse> createSubmitTaskResponse(SubmitTaskResponse value) {
        return new JAXBElement<SubmitTaskResponse>(_SubmitTaskResponse_QNAME, SubmitTaskResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSentTaskResultSms }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v1.ws.api.service.yxt.gooben.com/", name = "getSentTaskResultSms")
    public JAXBElement<GetSentTaskResultSms> createGetSentTaskResultSms(GetSentTaskResultSms value) {
        return new JAXBElement<GetSentTaskResultSms>(_GetSentTaskResultSms_QNAME, GetSentTaskResultSms.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSentResultSms }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v1.ws.api.service.yxt.gooben.com/", name = "getSentResultSms")
    public JAXBElement<GetSentResultSms> createGetSentResultSms(GetSentResultSms value) {
        return new JAXBElement<GetSentResultSms>(_GetSentResultSms_QNAME, GetSentResultSms.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Authorization }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v1.ws.api.service.yxt.gooben.com/", name = "authorization")
    public JAXBElement<Authorization> createAuthorization(Authorization value) {
        return new JAXBElement<Authorization>(_Authorization_QNAME, Authorization.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSubmitResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v1.ws.api.service.yxt.gooben.com/", name = "getSubmitResult")
    public JAXBElement<GetSubmitResult> createGetSubmitResult(GetSubmitResult value) {
        return new JAXBElement<GetSubmitResult>(_GetSubmitResult_QNAME, GetSubmitResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSentResultCallResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v1.ws.api.service.yxt.gooben.com/", name = "getSentResultCallResponse")
    public JAXBElement<GetSentResultCallResponse> createGetSentResultCallResponse(GetSentResultCallResponse value) {
        return new JAXBElement<GetSentResultCallResponse>(_GetSentResultCallResponse_QNAME, GetSentResultCallResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSentTaskResultSmsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v1.ws.api.service.yxt.gooben.com/", name = "getSentTaskResultSmsResponse")
    public JAXBElement<GetSentTaskResultSmsResponse> createGetSentTaskResultSmsResponse(GetSentTaskResultSmsResponse value) {
        return new JAXBElement<GetSentTaskResultSmsResponse>(_GetSentTaskResultSmsResponse_QNAME, GetSentTaskResultSmsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v1.ws.api.service.yxt.gooben.com/", name = "token")
    public JAXBElement<String> createToken(String value) {
        return new JAXBElement<String>(_Token_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSentTaskResultCall }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v1.ws.api.service.yxt.gooben.com/", name = "getSentTaskResultCall")
    public JAXBElement<GetSentTaskResultCall> createGetSentTaskResultCall(GetSentTaskResultCall value) {
        return new JAXBElement<GetSentTaskResultCall>(_GetSentTaskResultCall_QNAME, GetSentTaskResultCall.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthorizationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v1.ws.api.service.yxt.gooben.com/", name = "authorizationResponse")
    public JAXBElement<AuthorizationResponse> createAuthorizationResponse(AuthorizationResponse value) {
        return new JAXBElement<AuthorizationResponse>(_AuthorizationResponse_QNAME, AuthorizationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubmitTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v1.ws.api.service.yxt.gooben.com/", name = "submitTask")
    public JAXBElement<SubmitTask> createSubmitTask(SubmitTask value) {
        return new JAXBElement<SubmitTask>(_SubmitTask_QNAME, SubmitTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSubmitResultResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v1.ws.api.service.yxt.gooben.com/", name = "getSubmitResultResponse")
    public JAXBElement<GetSubmitResultResponse> createGetSubmitResultResponse(GetSubmitResultResponse value) {
        return new JAXBElement<GetSubmitResultResponse>(_GetSubmitResultResponse_QNAME, GetSubmitResultResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSentResultSmsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v1.ws.api.service.yxt.gooben.com/", name = "getSentResultSmsResponse")
    public JAXBElement<GetSentResultSmsResponse> createGetSentResultSmsResponse(GetSentResultSmsResponse value) {
        return new JAXBElement<GetSentResultSmsResponse>(_GetSentResultSmsResponse_QNAME, GetSentResultSmsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSmsReceive }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v1.ws.api.service.yxt.gooben.com/", name = "getSmsReceive")
    public JAXBElement<GetSmsReceive> createGetSmsReceive(GetSmsReceive value) {
        return new JAXBElement<GetSmsReceive>(_GetSmsReceive_QNAME, GetSmsReceive.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSmsReceiveResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://v1.ws.api.service.yxt.gooben.com/", name = "getSmsReceiveResponse")
    public JAXBElement<GetSmsReceiveResponse> createGetSmsReceiveResponse(GetSmsReceiveResponse value) {
        return new JAXBElement<GetSmsReceiveResponse>(_GetSmsReceiveResponse_QNAME, GetSmsReceiveResponse.class, null, value);
    }

}
