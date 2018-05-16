package hscsm.core.db.table


import com.hand.hap.liquibase.MigrationHelper

def mhi = MigrationHelper.getInstance()
dbType = MigrationHelper.getInstance().dbType()

databaseChangeLog(logicalFilePath:"yxhscs/core/db/2016-06-01-init-migration.groovy"){


    changeSet(author: "jessen", id: "20160601-hailor-1") {

        if(mhi.isDbType('oracle')){
            createSequence(sequenceName: 'HAP_DEMO_S')
        }
        createTable(tableName: "HAP_DEMO") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true")
            }

            column(name: "name", type: "VARCHAR(100)") {
            }

            column(name: "object_version_number", type: "BIGINT", defaultValue : "1")
            column(name: "request_id", type: "BIGINT", defaultValue : "-1")
            column(name: "program_id", type: "BIGINT", defaultValue : "-1")
            column(name: "created_by", type: "BIGINT", defaultValue : "-1")
            column(name: "creation_date", type: "DATETIME", defaultValueComputed : "CURRENT_TIMESTAMP")
            column(name: "last_updated_by", type: "BIGINT", defaultValue : "-1")
            column(name: "last_update_date", type: "DATETIME", defaultValueComputed : "CURRENT_TIMESTAMP")
            column(name: "last_update_login", type: "BIGINT", defaultValue : "-1")

        }

    }

    changeSet(author: "shuai.xie", id: "hmdm_init_table_YXHSCS_ITF_MON_APPLY") {
        sqlFile(path: mhi.dataPath("hscsm/core/db/data/" + dbType + "/tables/YXHSCS_ITF_MON_APPLY.sql"), encoding: "UTF-8")
    }
    changeSet(author: "shuai.xie", id: "hmdm_init_table_YXHSCS_ITF_PAN_APPLY") {
        sqlFile(path: mhi.dataPath("hscsm/core/db/data/" + dbType + "/tables/YXHSCS_ITF_PAN_APPLY.sql"), encoding: "UTF-8")
    }
    changeSet(author: "shuai.xie", id: "hmdm_init_table_YXHSCS_ITF_RCPT_LINE") {
        sqlFile(path: mhi.dataPath("hscsm/core/db/data/" + dbType + "/tables/YXHSCS_ITF_RCPT_LINE.sql"), encoding: "UTF-8")
    }
    changeSet(author: "shuai.xie", id: "hmdm_init_table_YXHSCS_ITF_CONTRACT_VALIDATED") {
        sqlFile(path: mhi.dataPath("hscsm/core/db/data/" + dbType + "/tables/YXHSCS_ITF_CONTRACT_VALIDATED.sql"), encoding: "UTF-8")
    }
    changeSet(author: "shuai.xie", id: "hmdm_init_table_YXHSCS_ITF_AR_INTERFACE") {
        sqlFile(path: mhi.dataPath("hscsm/core/db/data/" + dbType + "/tables/YXHSCS_ITF_AR_INTERFACE.sql"), encoding: "UTF-8")
    }

    changeSet(author: "shuai.xie", id: "hmdm_init_table_YXHSCS_ITF_MAL_CLEAR_INTERFACE") {
        sqlFile(path: mhi.dataPath("hscsm/core/db/data/" + dbType + "/tables/YXHSCS_ITF_MAL_CLEAR_INTERFACE.sql"), encoding: "UTF-8")
    }
//    changeSet(author: "junlin.zhu@hand-china.com", id: "hmdm_change_table_HSCS_AE_FEEDBACK_MESSAGE") {
//        sqlFile(path: mhi.dataPath("hscsm/core/db/data/" + dbType + "/tables/HSCS_AE_FEEDBACK_MESSAGE.sql"), encoding: "UTF-8")
//    }
    changeSet(author: "junlin.zhu@hand-china.com", id: "hmdm_init_table_YXHSCS_ITF_PSM_INV") {
        sqlFile(path: mhi.dataPath("hscsm/core/db/data/" + dbType + "/tables/YXHSCS_ITF_PSM_INV.sql"), encoding: "UTF-8")
    }
//    changeSet(author: "junlin.zhu@hand-china.com", id: "hmdm_change_table_HSCS_PUB_BANK_ACCOUNTS") {
//        sqlFile(path: mhi.dataPath("hscsm/core/db/data/" + dbType + "/tables/HSCS_PUB_BANK_ACCOUNTS.sql"), encoding: "UTF-8")
//    }


    changeSet(author: "junlin.zhu@hand-china.com", id: "hmdm_init_table_YXHSCS_PUB_ALIX_SUBCOM") {
        sqlFile(path: mhi.dataPath("hscsm/core/db/data/" + dbType + "/tables/YXHSCS_PUB_ALIX_SUBCOM.sql"), encoding: "UTF-8")
    }
    changeSet(author: "junlin.zhu@hand-china.com", id: "hmdm_init_table_YXHSCS_PUB_MAL_ACCOUNT") {
        sqlFile(path: mhi.dataPath("hscsm/core/db/data/" + dbType + "/tables/YXHSCS_PUB_MAL_ACCOUNT.sql"), encoding: "UTF-8")
    }

//    changeSet(author: "junlin.zhu@hand-china.com", id: "hmdm_change_table_HSCS_PUB_SOA_SEGMENT_VALUE_20180410") {
//        sqlFile(path: mhi.dataPath("hscsm/core/db/data/" + dbType + "/tables/HSCS_PUB_SOA_SEGMENT_VALUE.sql"), encoding: "UTF-8")
//    }

    changeSet(author: "junlin.zhu@hand-china.com", id: "hmdm_init_table_YXHSCS_MAL_VALI_WITHHOLD_20180412") {
        sqlFile(path: mhi.dataPath("hscsm/core/db/data/" + dbType + "/tables/YXHSCS_MAL_VALI_WITHHOLD.sql"), encoding: "UTF-8")
    }

    changeSet(author: "junlin.zhu@hand-china.com", id: "hmdm_init_table_YXHSCS_PUB_INVOICE_ENTITY_20180413") {
        sqlFile(path: mhi.dataPath("hscsm/core/db/data/" + dbType + "/tables/YXHSCS_PUB_INVOICE_ENTITY.sql"), encoding: "UTF-8")
    }

    changeSet(author: "junlin.zhu@hand-china.com", id: "hmdm_init_table_YXHSCS_SYS_VALUE_SET_20180414") {
        sqlFile(path: mhi.dataPath("hscsm/core/db/data/" + dbType + "/tables/YXHSCS_SYS_VALUE_SET.sql"), encoding: "UTF-8")
    }
}
