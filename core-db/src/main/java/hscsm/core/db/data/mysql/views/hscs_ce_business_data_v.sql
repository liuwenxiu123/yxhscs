CREATE VIEW hscs_ce_business_data_v AS
  SELECT
    'AR-营收' AS `AC_MODULE`,
    `harh`.`RECEIPT_HEADER_ID` AS `RECEIPT_HEADER_ID`,
    `harh`.`RECEIPT_NUMBER` AS `RECEIPT_NUMBER`,
    `harh`.`RECEIVE_TYPE_CODE` AS `RECEIVE_TYPE_CODE`,
    `harh`.`OPERATION_UNIT` AS `OPERATION_UNIT`,
    `harh`.`RECEIPT_AMOUNT` AS `RECEIPT_AMOUNT`,
    `harh`.`RECEIPT_BASEAMOUNT` AS `RECEIPT_BASEAMOUNT`,
    `harh`.`RECEIPT_DATE` AS `RECEIPT_DATE`,
    `harh`.`ORDER_NUMBER` AS `ORDER_NUMBER`,
    `harh`.`BANK_ID` AS `BANK_ID`,
    `harh`.`POUNDAGE_FEE` AS `POUNDAGE_FEE`,
    `harh`.`MERCHANT_ID` AS `MERCHANT_ID`,
    `harh`.`CURRENCY_ID` AS `CURRENCY_ID`,
    `harh`.`COMPANY_CODE` AS `COMPANY_CODE`,
    `hpssv`.`DESCRIPTION` AS `COMPANY_NAME`,
    `hppm`.`MERCHANT_NAME` AS `MERCHANT_NAME`,
    `harh`.`RECEIPT_METHOD` AS `RECEIPT_METHOD`,
    `harh`.`BANK_ACCOUNT_ID` AS `BANK_ACCOUNT_ID`,
    `hpba`.`ACCOUNT_NUMBER` AS `BANK_ACCOUNT_NUM`,
    `harh`.`REMMIT_BANK_ACCOUNT_ID` AS `REMMIT_BANK_ACCOUNT_ID`,
    `hpba`.`ACCOUNT_NUMBER` AS `REMMIT_BANK_ACCOUNT_NUM`,
    `harh`.`COMMENTS` AS `COMMENTS`,
    `hcfc`.`CHECK_DATE` AS `CHECK_DATE`,
    `hcfc`.`CHECK_NUMBER` AS `CHECK_NUMBER`
  FROM
    (
        (
            (
                (
                    `hscs_ar_receipt_headers` `harh`
                    LEFT JOIN `hscs_ce_flow_checks` `hcfc` ON (
                    (
                      `harh`.`FLOW_CHECK_ID` = `hcfc`.`FLOW_CHECK_ID`
                    )
                    )
                  )
                LEFT JOIN `hscs_pub_bank_accounts` `hpba` ON (
                (
                  `hpba`.`ACCOUNT_ID` = `harh`.`REMMIT_BANK_ACCOUNT_ID`
                )
                )
              )
            LEFT JOIN `hscs_pub_party_merchant` `hppm` ON (
            (
              `hppm`.`MERCHANT_ID` = `harh`.`MERCHANT_ID`
            )
            )
          )
        LEFT JOIN `hscs_pub_soa_segment_value` `hpssv` ON (
        (
          (
            `hpssv`.`SEGMENT_VALUE` = `harh`.`COMPANY_CODE`
          )
          AND (`hpssv`.`TYPE_CODE` = 'COM')
        )
        )
    )
  WHERE
    (
      (1 = 1)
      AND (
        isnull(`harh`.`UNKWN_CLAIM_FLAG`)
        OR (
          `harh`.`UNKWN_CLAIM_FLAG` <> 'Y'
        )
      )
      AND (
        `harh`.`RECEIPT_STATUS_CODE` <> 'DISMISSED'
      )
      AND (
        `harh`.`RECEIPT_STATUS_CODE` <> 'REVERSAL'
      )
    )
  UNION ALL
  SELECT
    'AP-采付' AS `AC_MODULE`,
    `haprh`.`RECEIPT_HEADER_ID` AS `RECEIPT_HEADER_ID`,
    `haprh`.`RECEIPT_NUMBER` AS `RECEIPT_NUMBER`,
    `haprh`.`RECEIVE_TYPE_CODE` AS `RECEIVE_TYPE_CODE`,
    `haprh`.`OPERATION_UNIT` AS `OPERATION_UNIT`,
    `haprh`.`RECEIPT_AMOUNT` AS `RECEIPT_AMOUNT`,
    `haprh`.`RECEIPT_BASEAMOUNT` AS `RECEIPT_BASEAMOUNT`,
    `haprh`.`RECEIPT_DATE` AS `RECEIPT_DATE`,
    `haprh`.`ORDER_NUMBER` AS `ORDER_NUMBER`,
    `haprh`.`MERCHANT_ID` AS `MERCHANT_ID`,
    `haprh`.`BANK_ID` AS `BANK_ID`,
    `haprh`.`POUNDAGE_FEE` AS `POUNDAGE_FEE`,
    `haprh`.`CURRENCY_ID` AS `CURRENCY_ID`,
    `haprh`.`COMPANY_CODE` AS `COMPANY_CODE`,
    `hpssv`.`DESCRIPTION` AS `COMPANY_NAME`,
    `hppm`.`MERCHANT_NAME` AS `MERCHANT_NAME`,
    `haprh`.`RECEIPT_METHOD` AS `RECEIPT_METHOD`,
    `haprh`.`BANK_ACCOUNT_ID` AS `BANK_ACCOUNT_ID`,
    `hpba`.`ACCOUNT_NUMBER` AS `BANK_ACCOUNT_NUM`,
    `haprh`.`REMMIT_BANK_ACCOUNT_ID` AS `REMMIT_BANK_ACCOUNT_ID`,
    `hpba`.`ACCOUNT_NUMBER` AS `REMMIT_BANK_ACCOUNT_NUM`,
    `haprh`.`COMMENTS` AS `COMMENTS`,
    `hcfc`.`CHECK_DATE` AS `CHECK_DATE`,
    `hcfc`.`CHECK_NUMBER` AS `CHECK_NUMBER`
  FROM
    (
        (
            (
                (
                    `hscs_ap_receipt_headers` `haprh`
                    LEFT JOIN `hscs_ce_flow_checks` `hcfc` ON (
                    (
                      `haprh`.`FLOW_CHECK_ID` = `hcfc`.`FLOW_CHECK_ID`
                    )
                    )
                  )
                LEFT JOIN `hscs_pub_bank_accounts` `hpba` ON (
                (
                  `hpba`.`ACCOUNT_ID` = `haprh`.`REMMIT_BANK_ACCOUNT_ID`
                )
                )
              )
            LEFT JOIN `hscs_pub_party_merchant` `hppm` ON (
            (
              `hppm`.`MERCHANT_ID` = `haprh`.`MERCHANT_ID`
            )
            )
          )
        LEFT JOIN `hscs_pub_soa_segment_value` `hpssv` ON (
        (
          (
            `hpssv`.`SEGMENT_VALUE` = `haprh`.`COMPANY_CODE`
          )
          AND (`hpssv`.`TYPE_CODE` = 'COM')
        )
        )
    )
  WHERE
    (
      (1 = 1)
      AND (
        isnull(`haprh`.`UNKWN_CLAIM_FLAG`)
        OR (
          `haprh`.`UNKWN_CLAIM_FLAG` <> 'Y'
        )
      )
      AND (
        `haprh`.`RECEIPT_STATUS_CODE` <> 'DISMISSED'
      )
      AND (
        `haprh`.`RECEIPT_STATUS_CODE` <> 'REVERSAL'
      )
    );
