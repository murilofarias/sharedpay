CREATE TABLE IF NOT EXISTS public.bill
(
    id bigint NOT NULL,
    additionals numeric(19,2),
    discounts numeric(19,2),
    has_waiter_service boolean,
    include_owner_payment boolean,
    cpf character varying(255) COLLATE pg_catalog."default",
    first_name character varying(255) COLLATE pg_catalog."default",
    last_name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT bill_pkey PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS public.individual_spending
(
    id bigint NOT NULL,
    cpf character varying(255) COLLATE pg_catalog."default",
    first_name character varying(255) COLLATE pg_catalog."default",
    last_name character varying(255) COLLATE pg_catalog."default",
    value numeric(19,2),
    bill_id bigint,
    CONSTRAINT individual_spending_pkey PRIMARY KEY (id),
    CONSTRAINT fkll0kmwpuqc8q8pgk7stxfkwwk FOREIGN KEY (bill_id)
        REFERENCES public.bill (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE TABLE IF NOT EXISTS public.payment
(
    id uuid NOT NULL,
    cpf character varying(255) COLLATE pg_catalog."default",
    first_name character varying(255) COLLATE pg_catalog."default",
    last_name character varying(255) COLLATE pg_catalog."default",
    fulfillment_date timestamp without time zone,
    payment_url character varying(255) COLLATE pg_catalog."default",
    status integer,
    value numeric(19,2),
    bill_id bigint,
    CONSTRAINT payment_pkey PRIMARY KEY (id),
    CONSTRAINT fkhdc173udjyonn4mt1lgt1x2ce FOREIGN KEY (bill_id)
        REFERENCES public.bill (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE TABLE IF NOT EXISTS public.user_account
(
    id bigint NOT NULL,
    credit numeric(19,2),
    cpf character varying(255) COLLATE pg_catalog."default",
    first_name character varying(255) COLLATE pg_catalog."default",
    last_name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT user_account_pkey PRIMARY KEY (id)
);

INSERT INTO bill( id, additionals, discounts, has_waiter_service, cpf, first_name, last_name)
VALUES (245, 8.00, 20.00, false, '3296161805', 'Alan', 'Santos');

INSERT INTO individual_spending(bill_id, cpf, first_name, id, last_name, "value")
VALUES(245, '61330346025', 'Alan', 456, 'Santos',  12.65);

INSERT INTO individual_spending(bill_id, cpf, first_name, id, last_name, "value")
VALUES(245, '60162452080', 'Brand', 457, 'Kay',  25.15 );

INSERT INTO individual_spending(bill_id, cpf, first_name, id, last_name, "value")
VALUES(245, '90542480093', 'Gilbert', 458, 'Jey',  31.92);

INSERT INTO payment(bill_id, cpf, first_name, fulfillment_date,id, last_name,payment_url,status, "value")
VALUES(245, '60162452080',  'Brand', NULL, '5a0f52e6-bbe0-40f1-ac2b-8ec329c2b1fb', 'Kay',  'https://app.picpay.com/checkout/NWZkOTK', 1, 20.22 );

INSERT INTO payment(bill_id, cpf, first_name, fulfillment_date,id, last_name,payment_url,status, "value")
VALUES(245, '90542480093',  'Gilbert', NULL, 'be68a4a5-587d-48ed-88c2-92797f00ae7d', 'Jey',  'https://app.picpay.com/checkout/NWZkOTA', 1, 35.22);

INSERT INTO user_account(
	id, credit, cpf, first_name, last_name)
	VALUES (1, 50.00, '61330346025', 'Alan', 'Santos');