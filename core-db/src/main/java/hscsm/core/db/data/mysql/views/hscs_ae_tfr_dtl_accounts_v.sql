CREATE OR REPLACE VIEW  hscs_ae_tfr_dtl_accounts_v AS
  SELECT
    `hscs_ae_tfr_dtl_accounts`.`TFR_DTL_ACCOUNT_ID` AS `TFR_DTL_ACCOUNT_ID`,
    `hscs_ae_tfr_dtl_accounts`.`TFR_EVENT_BATCH_ID` AS `TFR_EVENT_BATCH_ID`,
    `hscs_ae_tfr_dtl_accounts`.`TFR_EVENT_ID` AS `TFR_EVENT_ID`,
    `hscs_ae_tfr_dtl_accounts`.`SUMMARY_FLAG` AS `SUMMARY_FLAG`,
    `hscs_ae_tfr_dtl_accounts`.`EVENT_BATCH_ID` AS `EVENT_BATCH_ID`,
    `hscs_ae_tfr_dtl_accounts`.`EVENT_HEADER_ID` AS `EVENT_HEADER_ID`,
    `hscs_ae_tfr_dtl_accounts`.`ACCOUNT_HEADER_ID` AS `ACCOUNT_HEADER_ID`,
    `hscs_ae_tfr_dtl_accounts`.`RULE_HEADER_ID` AS `RULE_HEADER_ID`,
    `hscs_ae_tfr_dtl_accounts`.`TFR_SUM_ACCOUNT_ID` AS `TFR_SUM_ACCOUNT_ID`,
    `hscs_ae_tfr_dtl_accounts`.`ACCOUNTING_DATE` AS `ACCOUNTING_DATE`,
    `hscs_ae_tfr_dtl_accounts`.`ACCOUNTING_STATUS` AS `ACCOUNTING_STATUS`,
    `hscs_ae_tfr_dtl_accounts`.`ACCOUNTING_REMARKS` AS `ACCOUNTING_REMARKS`,
    `hscs_ae_tfr_dtl_accounts`.`REVERSAL_OBJECTS` AS `REVERSAL_OBJECTS`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE1`,
        1,
        240
    ) AS `VALUE1`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE2`,
        1,
        240
    ) AS `VALUE2`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE3`,
        1,
        240
    ) AS `VALUE3`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE4`,
        1,
        240
    ) AS `VALUE4`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE6`,
        1,
        240
    ) AS `VALUE6`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE7`,
        1,
        240
    ) AS `VALUE7`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE10`,
        1,
        240
    ) AS `VALUE10`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE11`,
        1,
        240
    ) AS `VALUE11`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE12`,
        1,
        240
    ) AS `SEGMENT1`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE13`,
        1,
        240
    ) AS `SEGMENT2`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE14`,
        1,
        240
    ) AS `SEGMENT3`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE15`,
        1,
        240
    ) AS `SEGMENT4`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE16`,
        1,
        240
    ) AS `SEGMENT5`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE17`,
        1,
        240
    ) AS `SEGMENT6`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE18`,
        1,
        240
    ) AS `SEGMENT7`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE19`,
        1,
        240
    ) AS `SEGMENT8`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE20`,
        1,
        240
    ) AS `SEGMENT9`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE21`,
        1,
        240
    ) AS `SEGMENT10`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE22`,
        1,
        240
    ) AS `SEGMENT11`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE23`,
        1,
        240
    ) AS `SEGMENT12`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE24`,
        1,
        240
    ) AS `SEGMENT13`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE25`,
        1,
        240
    ) AS `VALUE25`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE26`,
        1,
        240
    ) AS `VALUE26`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE27`,
        1,
        240
    ) AS `VALUE27`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE28`,
        1,
        240
    ) AS `VALUE28`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE29`,
        1,
        240
    ) AS `VALUE29`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE30`,
        1,
        240
    ) AS `VALUE30`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE31`,
        1,
        240
    ) AS `VALUE31`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE32`,
        1,
        240
    ) AS `VALUE32`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE33`,
        1,
        240
    ) AS `VALUE33`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE34`,
        1,
        240
    ) AS `VALUE34`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE35`,
        1,
        240
    ) AS `VALUE35`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE36`,
        1,
        240
    ) AS `VALUE36`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE37`,
        1,
        240
    ) AS `VALUE37`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE38`,
        1,
        240
    ) AS `VALUE38`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE39`,
        1,
        240
    ) AS `VALUE39`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE40`,
        1,
        240
    ) AS `VALUE40`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE41`,
        1,
        240
    ) AS `VALUE41`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE42`,
        1,
        240
    ) AS `VALUE42`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE43`,
        1,
        240
    ) AS `VALUE43`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE44`,
        1,
        240
    ) AS `VALUE44`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE45`,
        1,
        240
    ) AS `VALUE45`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE46`,
        1,
        240
    ) AS `VALUE46`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE47`,
        1,
        240
    ) AS `VALUE47`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE48`,
        1,
        240
    ) AS `VALUE48`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE49`,
        1,
        240
    ) AS `VALUE49`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE50`,
        1,
        240
    ) AS `VALUE50`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE51`,
        1,
        240
    ) AS `VALUE51`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE52`,
        1,
        240
    ) AS `VALUE52`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE53`,
        1,
        240
    ) AS `VALUE53`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE54`,
        1,
        240
    ) AS `VALUE54`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE55`,
        1,
        240
    ) AS `VALUE55`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE56`,
        1,
        240
    ) AS `VALUE56`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE57`,
        1,
        240
    ) AS `VALUE57`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE58`,
        1,
        240
    ) AS `VALUE58`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE59`,
        1,
        240
    ) AS `VALUE59`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE60`,
        1,
        240
    ) AS `VALUE60`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE61`,
        1,
        240
    ) AS `VALUE61`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE62`,
        1,
        240
    ) AS `VALUE62`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE63`,
        1,
        240
    ) AS `VALUE63`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE64`,
        1,
        240
    ) AS `VALUE64`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE65`,
        1,
        240
    ) AS `VALUE65`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE66`,
        1,
        240
    ) AS `VALUE66`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE67`,
        1,
        240
    ) AS `VALUE67`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE68`,
        1,
        240
    ) AS `VALUE68`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE69`,
        1,
        240
    ) AS `VALUE69`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE70`,
        1,
        240
    ) AS `VALUE70`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE71`,
        1,
        240
    ) AS `VALUE71`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE72`,
        1,
        240
    ) AS `VALUE72`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE73`,
        1,
        240
    ) AS `VALUE73`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE74`,
        1,
        240
    ) AS `VALUE74`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE75`,
        1,
        240
    ) AS `VALUE75`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE76`,
        1,
        240
    ) AS `VALUE76`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE77`,
        1,
        240
    ) AS `VALUE77`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE78`,
        1,
        240
    ) AS `VALUE78`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE79`,
        1,
        240
    ) AS `VALUE79`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE80`,
        1,
        240
    ) AS `VALUE80`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE81`,
        1,
        240
    ) AS `VALUE81`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE82`,
        1,
        240
    ) AS `VALUE82`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE83`,
        1,
        240
    ) AS `VALUE83`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE84`,
        1,
        240
    ) AS `VALUE84`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE85`,
        1,
        240
    ) AS `VALUE85`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE86`,
        1,
        240
    ) AS `VALUE86`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE87`,
        1,
        240
    ) AS `VALUE87`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE88`,
        1,
        240
    ) AS `VALUE88`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE89`,
        1,
        240
    ) AS `VALUE89`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE90`,
        1,
        240
    ) AS `VALUE90`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE91`,
        1,
        240
    ) AS `VALUE91`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE92`,
        1,
        240
    ) AS `VALUE92`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE93`,
        1,
        240
    ) AS `VALUE93`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE94`,
        1,
        240
    ) AS `VALUE94`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE95`,
        1,
        240
    ) AS `VALUE95`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE96`,
        1,
        240
    ) AS `VALUE96`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE97`,
        1,
        240
    ) AS `VALUE97`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE98`,
        1,
        240
    ) AS `VALUE98`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE99`,
        1,
        240
    ) AS `VALUE99`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE100`,
        1,
        240
    ) AS `VALUE100`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE101`,
        1,
        240
    ) AS `VALUE101`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE102`,
        1,
        240
    ) AS `VALUE102`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE103`,
        1,
        240
    ) AS `VALUE103`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE104`,
        1,
        240
    ) AS `VALUE104`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE105`,
        1,
        240
    ) AS `VALUE105`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE106`,
        1,
        240
    ) AS `VALUE106`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE107`,
        1,
        240
    ) AS `VALUE107`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE108`,
        1,
        240
    ) AS `VALUE108`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE109`,
        1,
        240
    ) AS `VALUE109`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE110`,
        1,
        240
    ) AS `VALUE110`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE111`,
        1,
        240
    ) AS `VALUE111`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE112`,
        1,
        240
    ) AS `VALUE112`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE113`,
        1,
        240
    ) AS `VALUE113`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE114`,
        1,
        240
    ) AS `VALUE114`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE115`,
        1,
        240
    ) AS `VALUE115`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE116`,
        1,
        240
    ) AS `VALUE116`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE117`,
        1,
        240
    ) AS `VALUE117`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE118`,
        1,
        240
    ) AS `VALUE118`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE119`,
        1,
        240
    ) AS `VALUE119`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE120`,
        1,
        240
    ) AS `VALUE120`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE121`,
        1,
        240
    ) AS `VALUE121`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE122`,
        1,
        240
    ) AS `VALUE122`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE123`,
        1,
        240
    ) AS `VALUE123`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE124`,
        1,
        240
    ) AS `VALUE124`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE125`,
        1,
        240
    ) AS `VALUE125`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE126`,
        1,
        240
    ) AS `VALUE126`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE127`,
        1,
        240
    ) AS `VALUE127`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE128`,
        1,
        240
    ) AS `VALUE128`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE129`,
        1,
        240
    ) AS `VALUE129`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE130`,
        1,
        240
    ) AS `VALUE130`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE131`,
        1,
        240
    ) AS `VALUE131`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE132`,
        1,
        240
    ) AS `VALUE132`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE133`,
        1,
        240
    ) AS `VALUE133`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE134`,
        1,
        240
    ) AS `VALUE134`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE135`,
        1,
        240
    ) AS `VALUE135`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE136`,
        1,
        240
    ) AS `VALUE136`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE137`,
        1,
        240
    ) AS `VALUE137`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE138`,
        1,
        240
    ) AS `VALUE138`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE139`,
        1,
        240
    ) AS `VALUE139`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE140`,
        1,
        240
    ) AS `VALUE140`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE141`,
        1,
        240
    ) AS `VALUE141`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE142`,
        1,
        240
    ) AS `VALUE142`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE143`,
        1,
        240
    ) AS `VALUE143`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE144`,
        1,
        240
    ) AS `VALUE144`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE145`,
        1,
        240
    ) AS `VALUE145`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE146`,
        1,
        240
    ) AS `VALUE146`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE147`,
        1,
        240
    ) AS `VALUE147`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE148`,
        1,
        240
    ) AS `VALUE148`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE149`,
        1,
        240
    ) AS `VALUE149`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE150`,
        1,
        240
    ) AS `VALUE150`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE151`,
        1,
        240
    ) AS `VALUE151`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE152`,
        1,
        240
    ) AS `VALUE152`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE153`,
        1,
        240
    ) AS `VALUE153`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE154`,
        1,
        240
    ) AS `VALUE154`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE155`,
        1,
        240
    ) AS `VALUE155`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE156`,
        1,
        240
    ) AS `VALUE156`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE157`,
        1,
        240
    ) AS `VALUE157`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE158`,
        1,
        240
    ) AS `VALUE158`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE159`,
        1,
        240
    ) AS `VALUE159`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE160`,
        1,
        240
    ) AS `VALUE160`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE161`,
        1,
        240
    ) AS `VALUE161`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE162`,
        1,
        240
    ) AS `VALUE162`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE163`,
        1,
        240
    ) AS `VALUE163`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE164`,
        1,
        240
    ) AS `VALUE164`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE165`,
        1,
        240
    ) AS `VALUE165`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE166`,
        1,
        240
    ) AS `VALUE166`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE167`,
        1,
        240
    ) AS `VALUE167`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE168`,
        1,
        240
    ) AS `VALUE168`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE169`,
        1,
        240
    ) AS `VALUE169`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE170`,
        1,
        240
    ) AS `VALUE170`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE171`,
        1,
        240
    ) AS `VALUE171`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE172`,
        1,
        240
    ) AS `VALUE172`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE173`,
        1,
        240
    ) AS `VALUE173`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE174`,
        1,
        240
    ) AS `VALUE174`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE175`,
        1,
        240
    ) AS `VALUE175`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE176`,
        1,
        240
    ) AS `VALUE176`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE177`,
        1,
        240
    ) AS `VALUE177`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE178`,
        1,
        240
    ) AS `VALUE178`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE179`,
        1,
        240
    ) AS `VALUE179`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE180`,
        1,
        240
    ) AS `VALUE180`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE181`,
        1,
        240
    ) AS `VALUE181`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE182`,
        1,
        240
    ) AS `VALUE182`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE183`,
        1,
        240
    ) AS `VALUE183`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE184`,
        1,
        240
    ) AS `VALUE184`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE185`,
        1,
        240
    ) AS `VALUE185`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE186`,
        1,
        240
    ) AS `VALUE186`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE187`,
        1,
        240
    ) AS `VALUE187`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE188`,
        1,
        240
    ) AS `VALUE188`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE189`,
        1,
        240
    ) AS `VALUE189`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE190`,
        1,
        240
    ) AS `VALUE190`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE191`,
        1,
        240
    ) AS `VALUE191`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE192`,
        1,
        240
    ) AS `VALUE192`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE193`,
        1,
        240
    ) AS `VALUE193`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE194`,
        1,
        240
    ) AS `VALUE194`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE195`,
        1,
        240
    ) AS `VALUE195`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE196`,
        1,
        240
    ) AS `VALUE196`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE197`,
        1,
        240
    ) AS `VALUE197`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE198`,
        1,
        240
    ) AS `VALUE198`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE199`,
        1,
        240
    ) AS `VALUE199`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE200`,
        1,
        240
    ) AS `VALUE200`,
    `hscs_ae_tfr_dtl_accounts`.`PROGRAM_ID` AS `PROGRAM_ID`,
    `hscs_ae_tfr_dtl_accounts`.`REQUEST_ID` AS `REQUEST_ID`,
    `hscs_ae_tfr_dtl_accounts`.`OBJECT_VERSION_NUMBER` AS `OBJECT_VERSION_NUMBER`,
    `hscs_ae_tfr_dtl_accounts`.`CREATED_BY` AS `CREATED_BY`,
    `hscs_ae_tfr_dtl_accounts`.`CREATION_DATE` AS `CREATION_DATE`,
    `hscs_ae_tfr_dtl_accounts`.`LAST_UPDATED_BY` AS `LAST_UPDATED_BY`,
    `hscs_ae_tfr_dtl_accounts`.`LAST_UPDATE_DATE` AS `LAST_UPDATE_DATE`,
    `hscs_ae_tfr_dtl_accounts`.`LAST_UPDATE_LOGIN` AS `LAST_UPDATE_LOGIN`,
    `hscs_ae_tfr_dtl_accounts`.`ATTRIBUTE_CATEGORY` AS `ATTRIBUTE_CATEGORY`,
    `hscs_ae_tfr_dtl_accounts`.`ATTRIBUTE1` AS `ATTRIBUTE1`,
    `hscs_ae_tfr_dtl_accounts`.`ATTRIBUTE2` AS `ATTRIBUTE2`,
    `hscs_ae_tfr_dtl_accounts`.`ATTRIBUTE3` AS `ATTRIBUTE3`,
    `hscs_ae_tfr_dtl_accounts`.`ATTRIBUTE4` AS `ATTRIBUTE4`,
    `hscs_ae_tfr_dtl_accounts`.`ATTRIBUTE5` AS `ATTRIBUTE5`,
    `hscs_ae_tfr_dtl_accounts`.`ATTRIBUTE6` AS `ATTRIBUTE6`,
    `hscs_ae_tfr_dtl_accounts`.`ATTRIBUTE7` AS `ATTRIBUTE7`,
    `hscs_ae_tfr_dtl_accounts`.`ATTRIBUTE8` AS `ATTRIBUTE8`,
    `hscs_ae_tfr_dtl_accounts`.`ATTRIBUTE9` AS `ATTRIBUTE9`,
    `hscs_ae_tfr_dtl_accounts`.`ATTRIBUTE10` AS `ATTRIBUTE10`,
    `hscs_ae_tfr_dtl_accounts`.`ATTRIBUTE11` AS `ATTRIBUTE11`,
    `hscs_ae_tfr_dtl_accounts`.`ATTRIBUTE12` AS `ATTRIBUTE12`,
    `hscs_ae_tfr_dtl_accounts`.`ATTRIBUTE13` AS `ATTRIBUTE13`,
    `hscs_ae_tfr_dtl_accounts`.`ATTRIBUTE14` AS `ATTRIBUTE14`,
    `hscs_ae_tfr_dtl_accounts`.`ATTRIBUTE15` AS `ATTRIBUTE15`
  FROM
    `hscs_ae_tfr_dtl_accounts`
  ORDER BY
    `hscs_ae_tfr_dtl_accounts`.`TFR_EVENT_ID`,
    substr(
        `hscs_ae_tfr_dtl_accounts`.`VALUE10`,
        1,
        240
    );