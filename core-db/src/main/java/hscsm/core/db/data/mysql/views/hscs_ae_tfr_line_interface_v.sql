CREATE VIEW hscs_ae_tfr_line_interface_v AS
  SELECT
    `hscs_ae_tfr_line_interface`.`TFR_INTERFACE_LINE_ID` AS `TFR_INTERFACE_LINE_ID`,
    `hscs_ae_tfr_line_interface`.`TFR_INTERFACE_ID` AS `TFR_INTERFACE_ID`,
    `hscs_ae_tfr_line_interface`.`EVENT_NAME` AS `EVENT_NAME`,
    `hscs_ae_tfr_line_interface`.`LINE_NUM` AS `LINE_NUM`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE1`,
        1,
        240
    ) AS `VALUE1`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE2`,
        1,
        240
    ) AS `VALUE2`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE3`,
        1,
        240
    ) AS `VALUE3`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE4`,
        1,
        240
    ) AS `VALUE4`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE5`,
        1,
        240
    ) AS `VALUE5`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE6`,
        1,
        240
    ) AS `VALUE6`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE7`,
        1,
        240
    ) AS `VALUE7`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE8`,
        1,
        240
    ) AS `VALUE8`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE9`,
        1,
        240
    ) AS `VALUE9`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE10`,
        1,
        240
    ) AS `VALUE10`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE11`,
        1,
        240
    ) AS `VALUE11`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE12`,
        1,
        240
    ) AS `VALUE12`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE13`,
        1,
        240
    ) AS `VALUE13`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE14`,
        1,
        240
    ) AS `VALUE14`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE15`,
        1,
        240
    ) AS `VALUE15`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE16`,
        1,
        240
    ) AS `VALUE16`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE17`,
        1,
        240
    ) AS `VALUE17`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE18`,
        1,
        240
    ) AS `VALUE18`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE19`,
        1,
        240
    ) AS `VALUE19`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE20`,
        1,
        240
    ) AS `VALUE20`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE21`,
        1,
        240
    ) AS `VALUE21`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE22`,
        1,
        240
    ) AS `VALUE22`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE23`,
        1,
        240
    ) AS `VALUE23`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE24`,
        1,
        240
    ) AS `VALUE24`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE25`,
        1,
        240
    ) AS `VALUE25`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE26`,
        1,
        240
    ) AS `VALUE26`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE27`,
        1,
        240
    ) AS `VALUE27`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE28`,
        1,
        240
    ) AS `VALUE28`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE29`,
        1,
        240
    ) AS `VALUE29`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE30`,
        1,
        240
    ) AS `VALUE30`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE31`,
        1,
        240
    ) AS `VALUE31`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE32`,
        1,
        240
    ) AS `VALUE32`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE33`,
        1,
        240
    ) AS `VALUE33`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE34`,
        1,
        240
    ) AS `VALUE34`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE35`,
        1,
        240
    ) AS `VALUE35`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE36`,
        1,
        240
    ) AS `VALUE36`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE37`,
        1,
        240
    ) AS `VALUE37`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE38`,
        1,
        240
    ) AS `VALUE38`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE39`,
        1,
        240
    ) AS `VALUE39`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE40`,
        1,
        240
    ) AS `VALUE40`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE41`,
        1,
        240
    ) AS `VALUE41`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE42`,
        1,
        240
    ) AS `VALUE42`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE43`,
        1,
        240
    ) AS `VALUE43`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE44`,
        1,
        240
    ) AS `VALUE44`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE45`,
        1,
        240
    ) AS `VALUE45`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE46`,
        1,
        240
    ) AS `VALUE46`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE47`,
        1,
        240
    ) AS `VALUE47`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE48`,
        1,
        240
    ) AS `VALUE48`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE49`,
        1,
        240
    ) AS `VALUE49`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE50`,
        1,
        240
    ) AS `VALUE50`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE51`,
        1,
        240
    ) AS `VALUE51`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE52`,
        1,
        240
    ) AS `VALUE52`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE53`,
        1,
        240
    ) AS `VALUE53`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE54`,
        1,
        240
    ) AS `VALUE54`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE55`,
        1,
        240
    ) AS `VALUE55`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE56`,
        1,
        240
    ) AS `VALUE56`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE57`,
        1,
        240
    ) AS `VALUE57`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE58`,
        1,
        240
    ) AS `VALUE58`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE59`,
        1,
        240
    ) AS `VALUE59`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE60`,
        1,
        240
    ) AS `VALUE60`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE61`,
        1,
        240
    ) AS `VALUE61`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE62`,
        1,
        240
    ) AS `VALUE62`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE63`,
        1,
        240
    ) AS `VALUE63`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE64`,
        1,
        240
    ) AS `VALUE64`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE65`,
        1,
        240
    ) AS `VALUE65`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE66`,
        1,
        240
    ) AS `VALUE66`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE67`,
        1,
        240
    ) AS `VALUE67`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE68`,
        1,
        240
    ) AS `VALUE68`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE69`,
        1,
        240
    ) AS `VALUE69`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE70`,
        1,
        240
    ) AS `VALUE70`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE71`,
        1,
        240
    ) AS `VALUE71`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE72`,
        1,
        240
    ) AS `VALUE72`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE73`,
        1,
        240
    ) AS `VALUE73`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE74`,
        1,
        240
    ) AS `VALUE74`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE75`,
        1,
        240
    ) AS `VALUE75`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE76`,
        1,
        240
    ) AS `VALUE76`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE77`,
        1,
        240
    ) AS `VALUE77`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE78`,
        1,
        240
    ) AS `VALUE78`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE79`,
        1,
        240
    ) AS `VALUE79`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE80`,
        1,
        240
    ) AS `VALUE80`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE81`,
        1,
        240
    ) AS `VALUE81`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE82`,
        1,
        240
    ) AS `VALUE82`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE83`,
        1,
        240
    ) AS `VALUE83`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE84`,
        1,
        240
    ) AS `VALUE84`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE85`,
        1,
        240
    ) AS `VALUE85`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE86`,
        1,
        240
    ) AS `VALUE86`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE87`,
        1,
        240
    ) AS `VALUE87`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE88`,
        1,
        240
    ) AS `VALUE88`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE89`,
        1,
        240
    ) AS `VALUE89`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE90`,
        1,
        240
    ) AS `VALUE90`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE91`,
        1,
        240
    ) AS `VALUE91`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE92`,
        1,
        240
    ) AS `VALUE92`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE93`,
        1,
        240
    ) AS `VALUE93`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE94`,
        1,
        240
    ) AS `VALUE94`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE95`,
        1,
        240
    ) AS `VALUE95`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE96`,
        1,
        240
    ) AS `VALUE96`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE97`,
        1,
        240
    ) AS `VALUE97`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE98`,
        1,
        240
    ) AS `VALUE98`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE99`,
        1,
        240
    ) AS `VALUE99`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE100`,
        1,
        240
    ) AS `VALUE100`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE101`,
        1,
        240
    ) AS `VALUE101`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE102`,
        1,
        240
    ) AS `VALUE102`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE103`,
        1,
        240
    ) AS `VALUE103`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE104`,
        1,
        240
    ) AS `VALUE104`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE105`,
        1,
        240
    ) AS `VALUE105`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE106`,
        1,
        240
    ) AS `VALUE106`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE107`,
        1,
        240
    ) AS `VALUE107`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE108`,
        1,
        240
    ) AS `VALUE108`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE109`,
        1,
        240
    ) AS `VALUE109`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE110`,
        1,
        240
    ) AS `VALUE110`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE111`,
        1,
        240
    ) AS `VALUE111`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE112`,
        1,
        240
    ) AS `VALUE112`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE113`,
        1,
        240
    ) AS `VALUE113`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE114`,
        1,
        240
    ) AS `VALUE114`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE115`,
        1,
        240
    ) AS `VALUE115`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE116`,
        1,
        240
    ) AS `VALUE116`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE117`,
        1,
        240
    ) AS `VALUE117`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE118`,
        1,
        240
    ) AS `VALUE118`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE119`,
        1,
        240
    ) AS `VALUE119`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE120`,
        1,
        240
    ) AS `VALUE120`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE121`,
        1,
        240
    ) AS `VALUE121`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE122`,
        1,
        240
    ) AS `VALUE122`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE123`,
        1,
        240
    ) AS `VALUE123`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE124`,
        1,
        240
    ) AS `VALUE124`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE125`,
        1,
        240
    ) AS `VALUE125`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE126`,
        1,
        240
    ) AS `VALUE126`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE127`,
        1,
        240
    ) AS `VALUE127`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE128`,
        1,
        240
    ) AS `VALUE128`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE129`,
        1,
        240
    ) AS `VALUE129`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE130`,
        1,
        240
    ) AS `VALUE130`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE131`,
        1,
        240
    ) AS `VALUE131`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE132`,
        1,
        240
    ) AS `VALUE132`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE133`,
        1,
        240
    ) AS `VALUE133`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE134`,
        1,
        240
    ) AS `VALUE134`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE135`,
        1,
        240
    ) AS `VALUE135`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE136`,
        1,
        240
    ) AS `VALUE136`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE137`,
        1,
        240
    ) AS `VALUE137`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE138`,
        1,
        240
    ) AS `VALUE138`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE139`,
        1,
        240
    ) AS `VALUE139`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE140`,
        1,
        240
    ) AS `VALUE140`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE141`,
        1,
        240
    ) AS `VALUE141`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE142`,
        1,
        240
    ) AS `VALUE142`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE143`,
        1,
        240
    ) AS `VALUE143`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE144`,
        1,
        240
    ) AS `VALUE144`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE145`,
        1,
        240
    ) AS `VALUE145`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE146`,
        1,
        240
    ) AS `VALUE146`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE147`,
        1,
        240
    ) AS `VALUE147`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE148`,
        1,
        240
    ) AS `VALUE148`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE149`,
        1,
        240
    ) AS `VALUE149`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE150`,
        1,
        240
    ) AS `VALUE150`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE151`,
        1,
        240
    ) AS `VALUE151`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE152`,
        1,
        240
    ) AS `VALUE152`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE153`,
        1,
        240
    ) AS `VALUE153`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE154`,
        1,
        240
    ) AS `VALUE154`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE155`,
        1,
        240
    ) AS `VALUE155`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE156`,
        1,
        240
    ) AS `VALUE156`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE157`,
        1,
        240
    ) AS `VALUE157`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE158`,
        1,
        240
    ) AS `VALUE158`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE159`,
        1,
        240
    ) AS `VALUE159`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE160`,
        1,
        240
    ) AS `VALUE160`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE161`,
        1,
        240
    ) AS `VALUE161`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE162`,
        1,
        240
    ) AS `VALUE162`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE163`,
        1,
        240
    ) AS `VALUE163`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE164`,
        1,
        240
    ) AS `VALUE164`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE165`,
        1,
        240
    ) AS `VALUE165`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE166`,
        1,
        240
    ) AS `VALUE166`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE167`,
        1,
        240
    ) AS `VALUE167`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE168`,
        1,
        240
    ) AS `VALUE168`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE169`,
        1,
        240
    ) AS `VALUE169`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE170`,
        1,
        240
    ) AS `VALUE170`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE171`,
        1,
        240
    ) AS `VALUE171`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE172`,
        1,
        240
    ) AS `VALUE172`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE173`,
        1,
        240
    ) AS `VALUE173`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE174`,
        1,
        240
    ) AS `VALUE174`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE175`,
        1,
        240
    ) AS `VALUE175`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE176`,
        1,
        240
    ) AS `VALUE176`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE177`,
        1,
        240
    ) AS `VALUE177`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE178`,
        1,
        240
    ) AS `VALUE178`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE179`,
        1,
        240
    ) AS `VALUE179`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE180`,
        1,
        240
    ) AS `VALUE180`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE181`,
        1,
        240
    ) AS `VALUE181`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE182`,
        1,
        240
    ) AS `VALUE182`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE183`,
        1,
        240
    ) AS `VALUE183`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE184`,
        1,
        240
    ) AS `VALUE184`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE185`,
        1,
        240
    ) AS `VALUE185`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE186`,
        1,
        240
    ) AS `VALUE186`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE187`,
        1,
        240
    ) AS `VALUE187`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE188`,
        1,
        240
    ) AS `VALUE188`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE189`,
        1,
        240
    ) AS `VALUE189`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE190`,
        1,
        240
    ) AS `VALUE190`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE191`,
        1,
        240
    ) AS `VALUE191`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE192`,
        1,
        240
    ) AS `VALUE192`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE193`,
        1,
        240
    ) AS `VALUE193`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE194`,
        1,
        240
    ) AS `VALUE194`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE195`,
        1,
        240
    ) AS `VALUE195`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE196`,
        1,
        240
    ) AS `VALUE196`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE197`,
        1,
        240
    ) AS `VALUE197`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE198`,
        1,
        240
    ) AS `VALUE198`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE199`,
        1,
        240
    ) AS `VALUE199`,
    substr(
        `hscs_ae_tfr_line_interface`.`VALUE200`,
        1,
        240
    ) AS `VALUE200`,
    `hscs_ae_tfr_line_interface`.`PROGRAM_ID` AS `PROGRAM_ID`,
    `hscs_ae_tfr_line_interface`.`REQUEST_ID` AS `REQUEST_ID`,
    `hscs_ae_tfr_line_interface`.`OBJECT_VERSION_NUMBER` AS `OBJECT_VERSION_NUMBER`,
    `hscs_ae_tfr_line_interface`.`CREATED_BY` AS `CREATED_BY`,
    `hscs_ae_tfr_line_interface`.`CREATION_DATE` AS `CREATION_DATE`,
    `hscs_ae_tfr_line_interface`.`LAST_UPDATED_BY` AS `LAST_UPDATED_BY`,
    `hscs_ae_tfr_line_interface`.`LAST_UPDATE_DATE` AS `LAST_UPDATE_DATE`,
    `hscs_ae_tfr_line_interface`.`LAST_UPDATE_LOGIN` AS `LAST_UPDATE_LOGIN`,
    `hscs_ae_tfr_line_interface`.`ATTRIBUTE_CATEGORY` AS `ATTRIBUTE_CATEGORY`,
    `hscs_ae_tfr_line_interface`.`ATTRIBUTE1` AS `ATTRIBUTE1`,
    `hscs_ae_tfr_line_interface`.`ATTRIBUTE2` AS `ATTRIBUTE2`,
    `hscs_ae_tfr_line_interface`.`ATTRIBUTE3` AS `ATTRIBUTE3`,
    `hscs_ae_tfr_line_interface`.`ATTRIBUTE4` AS `ATTRIBUTE4`,
    `hscs_ae_tfr_line_interface`.`ATTRIBUTE5` AS `ATTRIBUTE5`,
    `hscs_ae_tfr_line_interface`.`ATTRIBUTE6` AS `ATTRIBUTE6`,
    `hscs_ae_tfr_line_interface`.`ATTRIBUTE7` AS `ATTRIBUTE7`,
    `hscs_ae_tfr_line_interface`.`ATTRIBUTE8` AS `ATTRIBUTE8`,
    `hscs_ae_tfr_line_interface`.`ATTRIBUTE9` AS `ATTRIBUTE9`,
    `hscs_ae_tfr_line_interface`.`ATTRIBUTE10` AS `ATTRIBUTE10`,
    `hscs_ae_tfr_line_interface`.`ATTRIBUTE11` AS `ATTRIBUTE11`,
    `hscs_ae_tfr_line_interface`.`ATTRIBUTE12` AS `ATTRIBUTE12`,
    `hscs_ae_tfr_line_interface`.`ATTRIBUTE13` AS `ATTRIBUTE13`,
    `hscs_ae_tfr_line_interface`.`ATTRIBUTE14` AS `ATTRIBUTE14`,
    `hscs_ae_tfr_line_interface`.`ATTRIBUTE15` AS `ATTRIBUTE15`
  FROM
    `hscs_ae_tfr_line_interface`;