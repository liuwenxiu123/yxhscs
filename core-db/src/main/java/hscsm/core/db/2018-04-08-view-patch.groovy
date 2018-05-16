package hscsm.core.db


import com.hand.hap.liquibase.MigrationHelper

def mhi = MigrationHelper.getInstance()
dbType = MigrationHelper.getInstance().dbType()


databaseChangeLog(logicalFilePath: "yxhscs/core/db/2018-04-08-view-patch.groovy") {

//    changeSet(author: "shuai.xie", id: "hgxp_init_table_cux_mdm_organization_v") {
//        sqlFile(path: mhi.dataPath("yxhscs/core/db/data/" + dbType + "/views/cux_mdm_organization_v.sql"), encoding: "UTF-8")
//    }

//    changeSet(author: "junlin.zhu@hand-china.com", id: "hgxp_init_views_hscs_ae_tfr_dtl_accounts_v") {
//        sqlFile(path: mhi.dataPath("yxhscs/core/db/data/" + dbType + "/views/hscs_ae_tfr_dtl_accounts_v.sql"), encoding: "UTF-8")
//    }
//
//    changeSet(author: "junlin.zhu@hand-china.com", id: "hgxp_init_views_hscs_ae_tfr_line_interface_v") {
//        sqlFile(path: mhi.dataPath("yxhscs/core/db/data/" + dbType + "/views/hscs_ae_tfr_line_interface_v.sql"), encoding: "UTF-8")
//    }
//
//    changeSet(author: "junlin.zhu@hand-china.com", id: "hgxp_init_views_hscs_ce_business_data_v") {
//        sqlFile(path: mhi.dataPath("yxhscs/core/db/data/" + dbType + "/views/hscs_ce_business_data_v.sql"), encoding: "UTF-8")
//    }
//
//    changeSet(author: "junlin.zhu@hand-china.com", id: "hgxp_init_views_hscs_itf_imp_lines_v") {
//        sqlFile(path: mhi.dataPath("yxhscs/core/db/data/" + dbType + "/views/hscs_itf_imp_lines_v.sql"), encoding: "UTF-8")
//    }
}