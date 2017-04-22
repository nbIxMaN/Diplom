-- Sequence: public.fileinfo_id_seq
-- DROP SEQUENCE public.fileinfo_id_seq;
CREATE SEQUENCE public.fileinfo_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.fileinfo_id_seq
  OWNER TO postgres;


-- Sequence: public.user_id_seq
-- DROP SEQUENCE public.user_id_seq;
CREATE SEQUENCE public.user_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.user_id_seq
  OWNER TO postgres;


-- Table: public."user"
-- DROP TABLE public."user";
CREATE TABLE public."user"
(
  id bigint NOT NULL DEFAULT nextval('user_id_seq'::regclass),
  cookie character varying(255),
  login character varying(255),
  password character varying(255),
  CONSTRAINT user_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public."user"
  OWNER TO postgres;


-- Table: public.fileinfo
-- DROP TABLE public.fileinfo;
CREATE TABLE public.fileinfo
(
  id bigint NOT NULL DEFAULT nextval('fileinfo_id_seq'::regclass),
  name character varying(255),
  CONSTRAINT fileinfo_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.fileinfo
  OWNER TO postgres;


-- Table: public.user_fileinfo
-- DROP TABLE public.user_fileinfo;
CREATE TABLE public.user_fileinfo
(
  user_id bigint NOT NULL,
  setoffile_id bigint NOT NULL,
  CONSTRAINT user_fileinfo_pkey PRIMARY KEY (user_id, setoffile_id),
  CONSTRAINT fkmli5ryl21edjqey8s81755sqo FOREIGN KEY (user_id)
      REFERENCES public."user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fkpeos4aq4oo32qum2s6505mkls FOREIGN KEY (setoffile_id)
      REFERENCES public.fileinfo (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_3cao713drgal80akyymwl074f UNIQUE (setoffile_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.user_fileinfo
  OWNER TO postgres;
