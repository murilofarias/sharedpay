INSERT INTO bill( id, additionals, discounts, has_waiter_service, cpf, first_name, last_name)
VALUES (245, 8.00, 20.00, false, '3296161805', 'Alan', 'Santos');

INSERT INTO individual_spending(bill_id, cpf, first_name, id, last_name, "value")
VALUES(245, '3296161805', 'Alan', 456, 'Santos',  12.65);

INSERT INTO individual_spending(bill_id, cpf, first_name, id, last_name, "value")
VALUES(245, '60162452080', 'Brand', 457, 'Kay',  25.15 );

INSERT INTO individual_spending(bill_id, cpf, first_name, id, last_name, "value")
VALUES(245, '90542480093', 'Gilbert', 458, 'Jey',  31.92);

INSERT INTO payment(bill_id, cpf, first_name, fulfillment_date,id, last_name,payment_url,status, "value")
VALUES(245, '60162452080',  'Brand', NULL, 1204, 'Kay',  'https://app.picpay.com/checkout/NWZkOTK', 1, 20.22 );

INSERT INTO payment(bill_id, cpf, first_name, fulfillment_date,id, last_name,payment_url,status, "value")
VALUES(245, '90542480093',  'Gilbert', NULL, 1205, 'Jey',  'https://app.picpay.com/checkout/NWZkOTA', 1, 35.22);