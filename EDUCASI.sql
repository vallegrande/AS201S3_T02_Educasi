CREATE DATABASE DBEDUCASI;
USE DBEDUCASI;

CREATE TABLE ACTIVIDAD (
    IDACT int  NOT NULL IDENTITY(1,1),
    NOMACT varchar(60)  NOT NULL,
    MONESPACT int  NOT NULL,
    CANAPOACT int  NOT NULL,
    FECACT date  NOT NULL,
    ESTFINACT char(1) DEFAULT 'C' NOT NULL,
    ESTACT char(1) DEFAULT 'A'  NOT NULL,
    CONSTRAINT ACTIVIDAD_pk PRIMARY KEY  (IDACT)
);

-- Table: CRONOGRAMAS_COMUNICADOS
CREATE TABLE CRONOGRAMAS_COMUNICADOS (
    IDCRON int  NOT NULL IDENTITY(1,1),
    IMACRO binary(700)  NOT NULL,
    CONSTRAINT CRONOGRAMAS_COMUNICADOS_pk PRIMARY KEY  (IDCRON)
);

-- Table: CUOTA
CREATE TABLE CUOTA (
    IDCUOT int  NOT NULL IDENTITY(1,1),
    CANCUOT int  NOT NULL,
    MONCUOT int  NOT NULL,
    FECCUOT date  NOT NULL,
    IDACT int  NOT NULL,
    IDPER int  NOT NULL,
    CONSTRAINT CUOTA_pk PRIMARY KEY  (IDCUOT)
);

-- Table: GASTO_ACTIVIDAD
CREATE TABLE GASTO_ACTIVIDAD (
    IDGASACT int  NOT NULL IDENTITY(1,1),
    GASACT int  NOT NULL,
    DESGASACT varchar(60)  NOT NULL,
    FECGASACT date  NOT NULL,
    ESTGASACT char(1) DEFAULT 'A'  NOT NULL,
    IDACT int  NOT NULL,
    CONSTRAINT GASTO_ACTIVIDAD_pk PRIMARY KEY  (IDGASACT)
);

-- Table: PERSONA
CREATE TABLE PERSONA (
    IDPER int  NOT NULL IDENTITY(1,1),
    NOMPER varchar(20)  NOT NULL,
    APEPER varchar(20)  NOT NULL,
    PASPER varchar(16)  NOT NULL,
    EMAPER varchar(20)  NOT NULL,
    DIREPER varchar(20)  NOT NULL,
    DNIPER char(8)  NOT NULL,
    CELPER char(9)  NOT NULL,
    ROLPER char(9)  NOT NULL,
    ESTPER char(8)  NOT NULL,
    PERSONA_IDPER int  NULL,
    CONSTRAINT PERSONA_pk PRIMARY KEY  (IDPER)
);

-- foreign keys
-- Reference: CUOTA_ACTIVIDAD (table: CUOTA)
ALTER TABLE CUOTA ADD CONSTRAINT CUOTA_ACTIVIDAD
    FOREIGN KEY (IDACT)
    REFERENCES ACTIVIDAD (IDACT);

-- Reference: CUOTA_PERSONA (table: CUOTA)
ALTER TABLE CUOTA ADD CONSTRAINT CUOTA_PERSONA
    FOREIGN KEY (IDPER)
    REFERENCES PERSONA (IDPER);

-- Reference: PERSONA_PERSONA (table: PERSONA)
ALTER TABLE PERSONA ADD CONSTRAINT PERSONA_PERSONA
    FOREIGN KEY (PERSONA_IDPER)
    REFERENCES PERSONA (IDPER);

-- Reference: SALDO_ACTIVIDAD_ACTIVIDAD (table: GASTO_ACTIVIDAD)
ALTER TABLE GASTO_ACTIVIDAD ADD CONSTRAINT SALDO_ACTIVIDAD_ACTIVIDAD
    FOREIGN KEY (IDACT)
    REFERENCES ACTIVIDAD (IDACT);

-- vista inicio
CREATE VIEW V_CUOTA as
select IDCUOT,CANCUOT,MONCUOT,FECCUOT,CONCAT(NOMPER,' ',APEPER)AS NOMPER,NOMACT,ACTIVIDAD.IDACT,PERSONA.IDPER FROM CUOTA
inner join PERSONA ON CUOTA.IDPER=PERSONA.IDPER
inner join ACTIVIDAD ON CUOTA.IDACT=ACTIVIDAD.IDACT;
-- vista fin


-- vista inicio
CREATE VIEW V_PERSONA as
select ROW_NUMBER() OVER( ORDER BY super.IDPER desc) AS FILA, SUPER.IDPER,
super.NOMPER,super.APEPER,super.PASPER,
super.EMAPER,super.DIREPER,super.DNIPER,super.CELPER,
super.ROLPER,super.ESTPER,CONCAT(infer.NOMPER,' ',infer.APEPER)
as RELACION  from PERSONA  as super
left join PERSONA as infer on super.PERSONA_IDPER =infer.IDPER ;
-- vista fin

-- vista inicio
CREATE VIEW V_PERSONA_ROL as
select ROW_NUMBER() OVER( ORDER BY super.IDPER desc) AS FILA, SUPER.IDPER,
super.NOMPER,super.APEPER,super.PASPER,
super.EMAPER,super.DIREPER,super.DNIPER,super.CELPER,
super.ROLPER,super.ESTPER,CONCAT(infer.NOMPER,' ',infer.APEPER)
as RELACION  from PERSONA  as super
left join PERSONA as infer on super.PERSONA_IDPER =infer.IDPER;
-- vista fin

-- vista inicio
CREATE VIEW VW_GASTO_ACTIVIDAD
AS
SELECT GASTO_ACTIVIDAD.IDGASACT AS IDGASACT, GASTO_ACTIVIDAD.GASACT AS GASACT, GASTO_ACTIVIDAD.DESGASACT AS DESGASACT, 
GASTO_ACTIVIDAD.FECGASACT AS FECGASACT, GASTO_ACTIVIDAD.IDACT AS IDACT,ACTIVIDAD.NOMACT AS NOMACT, GASTO_ACTIVIDAD.ESTGASACT AS ESTGASACT 
FROM GASTO_ACTIVIDAD 
INNER JOIN ACTIVIDAD ON GASTO_ACTIVIDAD.IDACT = ACTIVIDAD.IDACT;
-- vista fin

--funcion
CREATE OR ALTER FUNCTION  SaldoCuota
( 
@idActividad integer,
@idPersona integer
)
RETURNS integer
    as 
	
    BEGIN
        declare @monto int;
		declare @acu int;
		select @acu = isnull(sum(MONCUOT),0) from CUOTA where IDACT=@idActividad and IDPER = @idPersona;
		select @monto =MONESPACT from ACTIVIDAD where IDACT=@idActividad;
        RETURN @monto - @acu;
    END;

	SELECT dbo.SaldoCuota(1,1) ;

