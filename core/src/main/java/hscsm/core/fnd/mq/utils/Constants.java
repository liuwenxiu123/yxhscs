package hscsm.core.fnd.mq.utils;

/**
 * Created by wangxu on 2018/3/3.
 */
public class Constants {

    // 序列主机地址
    public static final String SQUENCE_HOST="192.168.145.161";

    public static final int PROT=5672;

    public static final String EXCHANGE_TOPIC_TYPE="topic";

    public static final String ROUTING_KEY_ONE="import.data.fail";


    // alix 系统

    public static final String ALIX_V_HOST="alix_erp";

    public static final String ALIX_USER_NAME="guest";

    public static final String ALIX_PASSWORD="guest";

    public static final String ALIX_EXCHANGE_NAME="ALIX_SYSTEM";


    // ------------- 进销存--------------------

    public static final String ERP_V_HOST="rentcar";

    public static final String ERP_USER_NAME="rentcar_admin";

    public static final String ERP_PASSWORD="rentcar";

    public static final String ERP_EXCHANGE_NAME="RENTCAR_FOR_ERP";

    // ------------- 贷后--------------------

    public static final String FINALOAN_V_HOST="alix_erp";

    public static final String FINALOAN_USER_NAME="guest";

    public static final String FINALOAN_PASSWORD="guest";

    public static final String FINALOAN_EXCHANGE_NAME="FINALOAN_ERP";


    // ------------- 经租--------------------

    public static final String OPL_V_HOST="creditfrontol_mq";

    public static final String OPL_USER_NAME="rabbitadm";

    public static final String OPL_PASSWORD="admin";

    public static final String OPL_EXCHANGE_NAME="OPL_SYSTEM";


    // 成功的标志
    public static final String SUCEESS_FLAG="S";

    // 失败的标志
    public static final String FAILED_FLAG="E";


    // 参数错误
    public static final String PARAM_ERROR="E10000";
    // 报文解析异常
    public static final String MESSAGE_ERROR="E10001";
    //传输数据为空
    public static final String OTHER_ERROR="E10002";


    public static final  String ALIX_SYSTEM = "ALIX_SYSTEM"; // Alix系统

    public static final  String PSM_SYSTEM = "PSM_SYSTEM"; // 进销存系统

    public static final  String MAL_SYSTEM = "MAL_SYSTEM"; // 贷后系统

    public static final  String OPL_SYSTEM = "OPL_SYSTEM";// 经租系统


    public static final  String HEADER_DATA = "HEADER_DATA";// 经租系统

    public static final  String LINE_DATA = "LINE_DATA";// 经租系统




}
