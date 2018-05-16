CREATE VIEW hscs_itf_imp_lines_v AS
  SELECT
    `hscs_itf_imp_lines`.`LINE_ID` AS `LINE_ID`,
    `hscs_itf_imp_lines`.`HEADER_ID` AS `HEADER_ID`,
    `hscs_itf_imp_lines`.`SOURCE_ITERFACE_ID` AS `SOURCE_ITERFACE_ID`,
    `hscs_itf_imp_lines`.`PROCESS_DATE` AS `PROCESS_DATE`,
    `hscs_itf_imp_lines`.`IMPORT_STATUS` AS `IMPORT_STATUS`,
    `hscs_itf_imp_lines`.`PROCESS_STATUS` AS `PROCESS_STATUS`,
    `hscs_itf_imp_lines`.`PROCESS_GROUP` AS `PROCESS_GROUP`,
    `hscs_itf_imp_lines`.`STATEMENT_STATUS` AS `STATEMENT_STATUS`,
    `hscs_itf_imp_lines`.`STATEMENT_DATE` AS `STATEMENT_DATE`,
    substr(
        `hscs_itf_imp_lines`.`PROCESS_MESSAGE`,
        1,
        240
    ) AS `PROCESS_MESSAGE`,
    `hscs_itf_imp_lines`.`PROGRAM_ID` AS `PROGRAM_ID`,
    `hscs_itf_imp_lines`.`REQUEST_ID` AS `REQUEST_ID`,
    `hscs_itf_imp_lines`.`OBJECT_VERSION_NUMBER` AS `OBJECT_VERSION_NUMBER`,
    substr(
        `hscs_itf_imp_lines`.`VALUE1`,
        1,
        240
    ) AS `VALUE1`,
    substr(
        `hscs_itf_imp_lines`.`VALUE2`,
        1,
        240
    ) AS `VALUE2`,
    substr(
        `hscs_itf_imp_lines`.`VALUE3`,
        1,
        240
    ) AS `VALUE3`,
    substr(
        `hscs_itf_imp_lines`.`VALUE4`,
        1,
        240
    ) AS `VALUE4`,
    substr(
        `hscs_itf_imp_lines`.`VALUE5`,
        1,
        240
    ) AS `VALUE5`,
    substr(
        `hscs_itf_imp_lines`.`VALUE6`,
        1,
        240
    ) AS `VALUE6`,
    substr(
        `hscs_itf_imp_lines`.`VALUE7`,
        1,
        240
    ) AS `VALUE7`,
    substr(
        `hscs_itf_imp_lines`.`VALUE8`,
        1,
        240
    ) AS `VALUE8`,
    substr(
        `hscs_itf_imp_lines`.`VALUE9`,
        1,
        240
    ) AS `VALUE9`,
    substr(
        `hscs_itf_imp_lines`.`VALUE10`,
        1,
        240
    ) AS `VALUE10`,
    substr(
        `hscs_itf_imp_lines`.`VALUE11`,
        1,
        240
    ) AS `VALUE11`,
    substr(
        `hscs_itf_imp_lines`.`VALUE12`,
        1,
        240
    ) AS `VALUE12`,
    substr(
        `hscs_itf_imp_lines`.`VALUE13`,
        1,
        240
    ) AS `VALUE13`,
    substr(
        `hscs_itf_imp_lines`.`VALUE14`,
        1,
        240
    ) AS `VALUE14`,
    substr(
        `hscs_itf_imp_lines`.`VALUE15`,
        1,
        240
    ) AS `VALUE15`,
    substr(
        `hscs_itf_imp_lines`.`VALUE16`,
        1,
        240
    ) AS `VALUE16`,
    substr(
        `hscs_itf_imp_lines`.`VALUE17`,
        1,
        240
    ) AS `VALUE17`,
    substr(
        `hscs_itf_imp_lines`.`VALUE18`,
        1,
        240
    ) AS `VALUE18`,
    substr(
        `hscs_itf_imp_lines`.`VALUE19`,
        1,
        240
    ) AS `VALUE19`,
    substr(
        `hscs_itf_imp_lines`.`VALUE20`,
        1,
        240
    ) AS `VALUE20`,
    substr(
        `hscs_itf_imp_lines`.`VALUE21`,
        1,
        240
    ) AS `VALUE21`,
    substr(
        `hscs_itf_imp_lines`.`VALUE22`,
        1,
        240
    ) AS `VALUE22`,
    substr(
        `hscs_itf_imp_lines`.`VALUE23`,
        1,
        240
    ) AS `VALUE23`,
    substr(
        `hscs_itf_imp_lines`.`VALUE24`,
        1,
        240
    ) AS `VALUE24`,
    substr(
        `hscs_itf_imp_lines`.`VALUE25`,
        1,
        240
    ) AS `VALUE25`,
    substr(
        `hscs_itf_imp_lines`.`VALUE26`,
        1,
        240
    ) AS `VALUE26`,
    substr(
        `hscs_itf_imp_lines`.`VALUE27`,
        1,
        240
    ) AS `VALUE27`,
    substr(
        `hscs_itf_imp_lines`.`VALUE28`,
        1,
        240
    ) AS `VALUE28`,
    substr(
        `hscs_itf_imp_lines`.`VALUE29`,
        1,
        240
    ) AS `VALUE29`,
    substr(
        `hscs_itf_imp_lines`.`VALUE30`,
        1,
        240
    ) AS `VALUE30`,
    substr(
        `hscs_itf_imp_lines`.`VALUE31`,
        1,
        240
    ) AS `VALUE31`,
    substr(
        `hscs_itf_imp_lines`.`VALUE32`,
        1,
        240
    ) AS `VALUE32`,
    substr(
        `hscs_itf_imp_lines`.`VALUE33`,
        1,
        240
    ) AS `VALUE33`,
    substr(
        `hscs_itf_imp_lines`.`VALUE34`,
        1,
        240
    ) AS `VALUE34`,
    substr(
        `hscs_itf_imp_lines`.`VALUE35`,
        1,
        240
    ) AS `VALUE35`,
    substr(
        `hscs_itf_imp_lines`.`VALUE36`,
        1,
        240
    ) AS `VALUE36`,
    substr(
        `hscs_itf_imp_lines`.`VALUE37`,
        1,
        240
    ) AS `VALUE37`,
    substr(
        `hscs_itf_imp_lines`.`VALUE38`,
        1,
        240
    ) AS `VALUE38`,
    substr(
        `hscs_itf_imp_lines`.`VALUE39`,
        1,
        240
    ) AS `VALUE39`,
    substr(
        `hscs_itf_imp_lines`.`VALUE40`,
        1,
        240
    ) AS `VALUE40`,
    substr(
        `hscs_itf_imp_lines`.`VALUE41`,
        1,
        240
    ) AS `VALUE41`,
    substr(
        `hscs_itf_imp_lines`.`VALUE42`,
        1,
        240
    ) AS `VALUE42`,
    substr(
        `hscs_itf_imp_lines`.`VALUE43`,
        1,
        240
    ) AS `VALUE43`,
    substr(
        `hscs_itf_imp_lines`.`VALUE44`,
        1,
        240
    ) AS `VALUE44`,
    substr(
        `hscs_itf_imp_lines`.`VALUE45`,
        1,
        240
    ) AS `VALUE45`,
    substr(
        `hscs_itf_imp_lines`.`VALUE46`,
        1,
        240
    ) AS `VALUE46`,
    substr(
        `hscs_itf_imp_lines`.`VALUE47`,
        1,
        240
    ) AS `VALUE47`,
    substr(
        `hscs_itf_imp_lines`.`VALUE48`,
        1,
        240
    ) AS `VALUE48`,
    substr(
        `hscs_itf_imp_lines`.`VALUE49`,
        1,
        240
    ) AS `VALUE49`,
    substr(
        `hscs_itf_imp_lines`.`VALUE50`,
        1,
        240
    ) AS `VALUE50`,
    substr(
        `hscs_itf_imp_lines`.`VALUE51`,
        1,
        240
    ) AS `VALUE51`,
    substr(
        `hscs_itf_imp_lines`.`VALUE52`,
        1,
        240
    ) AS `VALUE52`,
    substr(
        `hscs_itf_imp_lines`.`VALUE53`,
        1,
        240
    ) AS `VALUE53`,
    substr(
        `hscs_itf_imp_lines`.`VALUE54`,
        1,
        240
    ) AS `VALUE54`,
    substr(
        `hscs_itf_imp_lines`.`VALUE55`,
        1,
        240
    ) AS `VALUE55`,
    substr(
        `hscs_itf_imp_lines`.`VALUE56`,
        1,
        240
    ) AS `VALUE56`,
    substr(
        `hscs_itf_imp_lines`.`VALUE57`,
        1,
        240
    ) AS `VALUE57`,
    substr(
        `hscs_itf_imp_lines`.`VALUE58`,
        1,
        240
    ) AS `VALUE58`,
    substr(
        `hscs_itf_imp_lines`.`VALUE59`,
        1,
        240
    ) AS `VALUE59`,
    substr(
        `hscs_itf_imp_lines`.`VALUE60`,
        1,
        240
    ) AS `VALUE60`,
    substr(
        `hscs_itf_imp_lines`.`VALUE61`,
        1,
        240
    ) AS `VALUE61`,
    substr(
        `hscs_itf_imp_lines`.`VALUE62`,
        1,
        240
    ) AS `VALUE62`,
    substr(
        `hscs_itf_imp_lines`.`VALUE63`,
        1,
        240
    ) AS `VALUE63`,
    substr(
        `hscs_itf_imp_lines`.`VALUE64`,
        1,
        240
    ) AS `VALUE64`,
    substr(
        `hscs_itf_imp_lines`.`VALUE65`,
        1,
        240
    ) AS `VALUE65`,
    substr(
        `hscs_itf_imp_lines`.`VALUE66`,
        1,
        240
    ) AS `VALUE66`,
    substr(
        `hscs_itf_imp_lines`.`VALUE67`,
        1,
        240
    ) AS `VALUE67`,
    substr(
        `hscs_itf_imp_lines`.`VALUE68`,
        1,
        240
    ) AS `VALUE68`,
    substr(
        `hscs_itf_imp_lines`.`VALUE69`,
        1,
        240
    ) AS `VALUE69`,
    substr(
        `hscs_itf_imp_lines`.`VALUE70`,
        1,
        240
    ) AS `VALUE70`,
    substr(
        `hscs_itf_imp_lines`.`VALUE71`,
        1,
        240
    ) AS `VALUE71`,
    substr(
        `hscs_itf_imp_lines`.`VALUE72`,
        1,
        240
    ) AS `VALUE72`,
    substr(
        `hscs_itf_imp_lines`.`VALUE73`,
        1,
        240
    ) AS `VALUE73`,
    substr(
        `hscs_itf_imp_lines`.`VALUE74`,
        1,
        240
    ) AS `VALUE74`,
    substr(
        `hscs_itf_imp_lines`.`VALUE75`,
        1,
        240
    ) AS `VALUE75`,
    substr(
        `hscs_itf_imp_lines`.`VALUE76`,
        1,
        240
    ) AS `VALUE76`,
    substr(
        `hscs_itf_imp_lines`.`VALUE77`,
        1,
        240
    ) AS `VALUE77`,
    substr(
        `hscs_itf_imp_lines`.`VALUE78`,
        1,
        240
    ) AS `VALUE78`,
    substr(
        `hscs_itf_imp_lines`.`VALUE79`,
        1,
        240
    ) AS `VALUE79`,
    substr(
        `hscs_itf_imp_lines`.`VALUE80`,
        1,
        240
    ) AS `VALUE80`,
    substr(
        `hscs_itf_imp_lines`.`VALUE81`,
        1,
        240
    ) AS `VALUE81`,
    substr(
        `hscs_itf_imp_lines`.`VALUE82`,
        1,
        240
    ) AS `VALUE82`,
    substr(
        `hscs_itf_imp_lines`.`VALUE83`,
        1,
        240
    ) AS `VALUE83`,
    substr(
        `hscs_itf_imp_lines`.`VALUE84`,
        1,
        240
    ) AS `VALUE84`,
    substr(
        `hscs_itf_imp_lines`.`VALUE85`,
        1,
        240
    ) AS `VALUE85`,
    substr(
        `hscs_itf_imp_lines`.`VALUE86`,
        1,
        240
    ) AS `VALUE86`,
    substr(
        `hscs_itf_imp_lines`.`VALUE87`,
        1,
        240
    ) AS `VALUE87`,
    substr(
        `hscs_itf_imp_lines`.`VALUE88`,
        1,
        240
    ) AS `VALUE88`,
    substr(
        `hscs_itf_imp_lines`.`VALUE89`,
        1,
        240
    ) AS `VALUE89`,
    substr(
        `hscs_itf_imp_lines`.`VALUE90`,
        1,
        240
    ) AS `VALUE90`,
    substr(
        `hscs_itf_imp_lines`.`VALUE91`,
        1,
        240
    ) AS `VALUE91`,
    substr(
        `hscs_itf_imp_lines`.`VALUE92`,
        1,
        240
    ) AS `VALUE92`,
    substr(
        `hscs_itf_imp_lines`.`VALUE93`,
        1,
        240
    ) AS `VALUE93`,
    substr(
        `hscs_itf_imp_lines`.`VALUE94`,
        1,
        240
    ) AS `VALUE94`,
    substr(
        `hscs_itf_imp_lines`.`VALUE95`,
        1,
        240
    ) AS `VALUE95`,
    substr(
        `hscs_itf_imp_lines`.`VALUE96`,
        1,
        240
    ) AS `VALUE96`,
    substr(
        `hscs_itf_imp_lines`.`VALUE97`,
        1,
        240
    ) AS `VALUE97`,
    substr(
        `hscs_itf_imp_lines`.`VALUE98`,
        1,
        240
    ) AS `VALUE98`,
    substr(
        `hscs_itf_imp_lines`.`VALUE99`,
        1,
        240
    ) AS `VALUE99`,
    substr(
        `hscs_itf_imp_lines`.`VALUE100`,
        1,
        240
    ) AS `VALUE100`,
    substr(
        `hscs_itf_imp_lines`.`VALUE101`,
        1,
        240
    ) AS `VALUE101`,
    substr(
        `hscs_itf_imp_lines`.`VALUE102`,
        1,
        240
    ) AS `VALUE102`,
    substr(
        `hscs_itf_imp_lines`.`VALUE103`,
        1,
        240
    ) AS `VALUE103`,
    substr(
        `hscs_itf_imp_lines`.`VALUE104`,
        1,
        240
    ) AS `VALUE104`,
    substr(
        `hscs_itf_imp_lines`.`VALUE105`,
        1,
        240
    ) AS `VALUE105`,
    substr(
        `hscs_itf_imp_lines`.`VALUE106`,
        1,
        240
    ) AS `VALUE106`,
    substr(
        `hscs_itf_imp_lines`.`VALUE107`,
        1,
        240
    ) AS `VALUE107`,
    substr(
        `hscs_itf_imp_lines`.`VALUE108`,
        1,
        240
    ) AS `VALUE108`,
    substr(
        `hscs_itf_imp_lines`.`VALUE109`,
        1,
        240
    ) AS `VALUE109`,
    substr(
        `hscs_itf_imp_lines`.`VALUE110`,
        1,
        240
    ) AS `VALUE110`,
    substr(
        `hscs_itf_imp_lines`.`VALUE111`,
        1,
        240
    ) AS `VALUE111`,
    substr(
        `hscs_itf_imp_lines`.`VALUE112`,
        1,
        240
    ) AS `VALUE112`,
    substr(
        `hscs_itf_imp_lines`.`VALUE113`,
        1,
        240
    ) AS `VALUE113`,
    substr(
        `hscs_itf_imp_lines`.`VALUE114`,
        1,
        240
    ) AS `VALUE114`,
    substr(
        `hscs_itf_imp_lines`.`VALUE115`,
        1,
        240
    ) AS `VALUE115`,
    substr(
        `hscs_itf_imp_lines`.`VALUE116`,
        1,
        240
    ) AS `VALUE116`,
    substr(
        `hscs_itf_imp_lines`.`VALUE117`,
        1,
        240
    ) AS `VALUE117`,
    substr(
        `hscs_itf_imp_lines`.`VALUE118`,
        1,
        240
    ) AS `VALUE118`,
    substr(
        `hscs_itf_imp_lines`.`VALUE119`,
        1,
        240
    ) AS `VALUE119`,
    substr(
        `hscs_itf_imp_lines`.`VALUE120`,
        1,
        240
    ) AS `VALUE120`,
    substr(
        `hscs_itf_imp_lines`.`VALUE121`,
        1,
        240
    ) AS `VALUE121`,
    substr(
        `hscs_itf_imp_lines`.`VALUE122`,
        1,
        240
    ) AS `VALUE122`,
    substr(
        `hscs_itf_imp_lines`.`VALUE123`,
        1,
        240
    ) AS `VALUE123`,
    substr(
        `hscs_itf_imp_lines`.`VALUE124`,
        1,
        240
    ) AS `VALUE124`,
    substr(
        `hscs_itf_imp_lines`.`VALUE125`,
        1,
        240
    ) AS `VALUE125`,
    substr(
        `hscs_itf_imp_lines`.`VALUE126`,
        1,
        240
    ) AS `VALUE126`,
    substr(
        `hscs_itf_imp_lines`.`VALUE127`,
        1,
        240
    ) AS `VALUE127`,
    substr(
        `hscs_itf_imp_lines`.`VALUE128`,
        1,
        240
    ) AS `VALUE128`,
    substr(
        `hscs_itf_imp_lines`.`VALUE129`,
        1,
        240
    ) AS `VALUE129`,
    substr(
        `hscs_itf_imp_lines`.`VALUE130`,
        1,
        240
    ) AS `VALUE130`,
    substr(
        `hscs_itf_imp_lines`.`VALUE131`,
        1,
        240
    ) AS `VALUE131`,
    substr(
        `hscs_itf_imp_lines`.`VALUE132`,
        1,
        240
    ) AS `VALUE132`,
    substr(
        `hscs_itf_imp_lines`.`VALUE133`,
        1,
        240
    ) AS `VALUE133`,
    substr(
        `hscs_itf_imp_lines`.`VALUE134`,
        1,
        240
    ) AS `VALUE134`,
    substr(
        `hscs_itf_imp_lines`.`VALUE135`,
        1,
        240
    ) AS `VALUE135`,
    substr(
        `hscs_itf_imp_lines`.`VALUE136`,
        1,
        240
    ) AS `VALUE136`,
    substr(
        `hscs_itf_imp_lines`.`VALUE137`,
        1,
        240
    ) AS `VALUE137`,
    substr(
        `hscs_itf_imp_lines`.`VALUE138`,
        1,
        240
    ) AS `VALUE138`,
    substr(
        `hscs_itf_imp_lines`.`VALUE139`,
        1,
        240
    ) AS `VALUE139`,
    substr(
        `hscs_itf_imp_lines`.`VALUE140`,
        1,
        240
    ) AS `VALUE140`,
    substr(
        `hscs_itf_imp_lines`.`VALUE141`,
        1,
        240
    ) AS `VALUE141`,
    substr(
        `hscs_itf_imp_lines`.`VALUE142`,
        1,
        240
    ) AS `VALUE142`,
    substr(
        `hscs_itf_imp_lines`.`VALUE143`,
        1,
        240
    ) AS `VALUE143`,
    substr(
        `hscs_itf_imp_lines`.`VALUE144`,
        1,
        240
    ) AS `VALUE144`,
    substr(
        `hscs_itf_imp_lines`.`VALUE145`,
        1,
        240
    ) AS `VALUE145`,
    substr(
        `hscs_itf_imp_lines`.`VALUE146`,
        1,
        240
    ) AS `VALUE146`,
    substr(
        `hscs_itf_imp_lines`.`VALUE147`,
        1,
        240
    ) AS `VALUE147`,
    substr(
        `hscs_itf_imp_lines`.`VALUE148`,
        1,
        240
    ) AS `VALUE148`,
    substr(
        `hscs_itf_imp_lines`.`VALUE149`,
        1,
        240
    ) AS `VALUE149`,
    substr(
        `hscs_itf_imp_lines`.`VALUE150`,
        1,
        240
    ) AS `VALUE150`,
    substr(
        `hscs_itf_imp_lines`.`VALUE151`,
        1,
        240
    ) AS `VALUE151`,
    substr(
        `hscs_itf_imp_lines`.`VALUE152`,
        1,
        240
    ) AS `VALUE152`,
    substr(
        `hscs_itf_imp_lines`.`VALUE153`,
        1,
        240
    ) AS `VALUE153`,
    substr(
        `hscs_itf_imp_lines`.`VALUE154`,
        1,
        240
    ) AS `VALUE154`,
    substr(
        `hscs_itf_imp_lines`.`VALUE155`,
        1,
        240
    ) AS `VALUE155`,
    substr(
        `hscs_itf_imp_lines`.`VALUE156`,
        1,
        240
    ) AS `VALUE156`,
    substr(
        `hscs_itf_imp_lines`.`VALUE157`,
        1,
        240
    ) AS `VALUE157`,
    substr(
        `hscs_itf_imp_lines`.`VALUE158`,
        1,
        240
    ) AS `VALUE158`,
    substr(
        `hscs_itf_imp_lines`.`VALUE159`,
        1,
        240
    ) AS `VALUE159`,
    substr(
        `hscs_itf_imp_lines`.`VALUE160`,
        1,
        240
    ) AS `VALUE160`,
    substr(
        `hscs_itf_imp_lines`.`VALUE161`,
        1,
        240
    ) AS `VALUE161`,
    substr(
        `hscs_itf_imp_lines`.`VALUE162`,
        1,
        240
    ) AS `VALUE162`,
    substr(
        `hscs_itf_imp_lines`.`VALUE163`,
        1,
        240
    ) AS `VALUE163`,
    substr(
        `hscs_itf_imp_lines`.`VALUE164`,
        1,
        240
    ) AS `VALUE164`,
    substr(
        `hscs_itf_imp_lines`.`VALUE165`,
        1,
        240
    ) AS `VALUE165`,
    substr(
        `hscs_itf_imp_lines`.`VALUE166`,
        1,
        240
    ) AS `VALUE166`,
    substr(
        `hscs_itf_imp_lines`.`VALUE167`,
        1,
        240
    ) AS `VALUE167`,
    substr(
        `hscs_itf_imp_lines`.`VALUE168`,
        1,
        240
    ) AS `VALUE168`,
    substr(
        `hscs_itf_imp_lines`.`VALUE169`,
        1,
        240
    ) AS `VALUE169`,
    substr(
        `hscs_itf_imp_lines`.`VALUE170`,
        1,
        240
    ) AS `VALUE170`,
    substr(
        `hscs_itf_imp_lines`.`VALUE171`,
        1,
        240
    ) AS `VALUE171`,
    substr(
        `hscs_itf_imp_lines`.`VALUE172`,
        1,
        240
    ) AS `VALUE172`,
    substr(
        `hscs_itf_imp_lines`.`VALUE173`,
        1,
        240
    ) AS `VALUE173`,
    substr(
        `hscs_itf_imp_lines`.`VALUE174`,
        1,
        240
    ) AS `VALUE174`,
    substr(
        `hscs_itf_imp_lines`.`VALUE175`,
        1,
        240
    ) AS `VALUE175`,
    substr(
        `hscs_itf_imp_lines`.`VALUE176`,
        1,
        240
    ) AS `VALUE176`,
    substr(
        `hscs_itf_imp_lines`.`VALUE177`,
        1,
        240
    ) AS `VALUE177`,
    substr(
        `hscs_itf_imp_lines`.`VALUE178`,
        1,
        240
    ) AS `VALUE178`,
    substr(
        `hscs_itf_imp_lines`.`VALUE179`,
        1,
        240
    ) AS `VALUE179`,
    substr(
        `hscs_itf_imp_lines`.`VALUE180`,
        1,
        240
    ) AS `VALUE180`,
    substr(
        `hscs_itf_imp_lines`.`VALUE181`,
        1,
        240
    ) AS `VALUE181`,
    substr(
        `hscs_itf_imp_lines`.`VALUE182`,
        1,
        240
    ) AS `VALUE182`,
    substr(
        `hscs_itf_imp_lines`.`VALUE183`,
        1,
        240
    ) AS `VALUE183`,
    substr(
        `hscs_itf_imp_lines`.`VALUE184`,
        1,
        240
    ) AS `VALUE184`,
    substr(
        `hscs_itf_imp_lines`.`VALUE185`,
        1,
        240
    ) AS `VALUE185`,
    substr(
        `hscs_itf_imp_lines`.`VALUE186`,
        1,
        240
    ) AS `VALUE186`,
    substr(
        `hscs_itf_imp_lines`.`VALUE187`,
        1,
        240
    ) AS `VALUE187`,
    substr(
        `hscs_itf_imp_lines`.`VALUE188`,
        1,
        240
    ) AS `VALUE188`,
    substr(
        `hscs_itf_imp_lines`.`VALUE189`,
        1,
        240
    ) AS `VALUE189`,
    substr(
        `hscs_itf_imp_lines`.`VALUE190`,
        1,
        240
    ) AS `VALUE190`,
    substr(
        `hscs_itf_imp_lines`.`VALUE191`,
        1,
        240
    ) AS `VALUE191`,
    substr(
        `hscs_itf_imp_lines`.`VALUE192`,
        1,
        240
    ) AS `VALUE192`,
    substr(
        `hscs_itf_imp_lines`.`VALUE193`,
        1,
        240
    ) AS `VALUE193`,
    substr(
        `hscs_itf_imp_lines`.`VALUE194`,
        1,
        240
    ) AS `VALUE194`,
    substr(
        `hscs_itf_imp_lines`.`VALUE195`,
        1,
        240
    ) AS `VALUE195`,
    substr(
        `hscs_itf_imp_lines`.`VALUE196`,
        1,
        240
    ) AS `VALUE196`,
    substr(
        `hscs_itf_imp_lines`.`VALUE197`,
        1,
        240
    ) AS `VALUE197`,
    substr(
        `hscs_itf_imp_lines`.`VALUE198`,
        1,
        240
    ) AS `VALUE198`,
    substr(
        `hscs_itf_imp_lines`.`VALUE199`,
        1,
        240
    ) AS `VALUE199`,
    substr(
        `hscs_itf_imp_lines`.`VALUE200`,
        1,
        240
    ) AS `VALUE200`,
    `hscs_itf_imp_lines`.`CREATION_DATE` AS `CREATION_DATE`,
    `hscs_itf_imp_lines`.`CREATED_BY` AS `CREATED_BY`,
    `hscs_itf_imp_lines`.`LAST_UPDATED_BY` AS `LAST_UPDATED_BY`,
    `hscs_itf_imp_lines`.`LAST_UPDATE_DATE` AS `LAST_UPDATE_DATE`,
    `hscs_itf_imp_lines`.`LAST_UPDATE_LOGIN` AS `LAST_UPDATE_LOGIN`,
    `hscs_itf_imp_lines`.`ATTRIBUTE_CATEGORY` AS `ATTRIBUTE_CATEGORY`,
    `hscs_itf_imp_lines`.`ATTRIBUTE1` AS `ATTRIBUTE1`,
    `hscs_itf_imp_lines`.`ATTRIBUTE2` AS `ATTRIBUTE2`,
    `hscs_itf_imp_lines`.`ATTRIBUTE3` AS `ATTRIBUTE3`,
    `hscs_itf_imp_lines`.`ATTRIBUTE4` AS `ATTRIBUTE4`,
    `hscs_itf_imp_lines`.`ATTRIBUTE5` AS `ATTRIBUTE5`,
    `hscs_itf_imp_lines`.`ATTRIBUTE6` AS `ATTRIBUTE6`,
    `hscs_itf_imp_lines`.`ATTRIBUTE7` AS `ATTRIBUTE7`,
    `hscs_itf_imp_lines`.`ATTRIBUTE8` AS `ATTRIBUTE8`,
    `hscs_itf_imp_lines`.`ATTRIBUTE9` AS `ATTRIBUTE9`,
    `hscs_itf_imp_lines`.`ATTRIBUTE10` AS `ATTRIBUTE10`,
    `hscs_itf_imp_lines`.`ATTRIBUTE11` AS `ATTRIBUTE11`,
    `hscs_itf_imp_lines`.`ATTRIBUTE12` AS `ATTRIBUTE12`,
    `hscs_itf_imp_lines`.`ATTRIBUTE13` AS `ATTRIBUTE13`,
    `hscs_itf_imp_lines`.`ATTRIBUTE14` AS `ATTRIBUTE14`,
    `hscs_itf_imp_lines`.`ATTRIBUTE15` AS `ATTRIBUTE15`
  FROM
    `hscs_itf_imp_lines`;