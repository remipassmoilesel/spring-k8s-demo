package org.remipassmoilesel.k8sdemo;

public class Routes {

    public static final String ROOT = "/";

    public static final String API_ROOT = ROOT + "api";

    public static final String APP_ROOT = API_ROOT + "/app";
    public static final String APP_IDENTITY = APP_ROOT + "/identity";

    public static final String DOC_MANAGEMENT_ROOT = API_ROOT + "/document-management";
    public static final String DOCUMENTS = DOC_MANAGEMENT_ROOT + "/documents";

    public static final String SIGN_MANAGEMENT_ROOT = API_ROOT + "/signature-management";
    public static final String DOC_SIGNATURE = SIGN_MANAGEMENT_ROOT + "/document";

}
