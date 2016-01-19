# JMC

###Descripción general

Utileria escrita en java, para obtener ciertos datos de un sitio, de acuerdo al sitio se hace la clase que contiene el procedimiento para hacerlo. Actualmente obtiene los siguientes datos de una noticia:

- Titulo.
- Autor.
- Fecha de publicación.
- Contenido.
- Enlace.

La información se guarda en una base de datos relacional en las siguientes tablas.

´´´
-- Tabla donde se guarda el contenido de la noticia
CREATE TABLE contenido
(
  cont_id numeric(10,0) NOT NULL,
  cont_titulo character varying(500) NOT NULL,
  cont_contenido character varying(100000) NOT NULL,
  cont_autor character varying(1000) NOT NULL,
  cont_enlace character varying(1000) NOT NULL,
  cont_fecha date,
  cont_sitio numeric NOT NULL,
  CONSTRAINT pk_cont_contenido PRIMARY KEY (cont_id),
  CONSTRAINT fk_sitio_csitio FOREIGN KEY (cont_sitio)
      REFERENCES csitio (csit_sitio) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Catálogo de sitios soportados
CREATE TABLE csitio
(
  csit_sitio numeric NOT NULL,
  csit_descrip character varying(1000) NOT NULL,
  CONSTRAINT "PK_SITIO" PRIMARY KEY (csit_sitio)
);

-- Tabla donde se guardan los datos de comentarios de facebook
CREATE TABLE fbcoment
(
  fbco_contenido numeric NOT NULL,
  fbco_nombre character varying(100) NOT NULL,
  fbco_idn numeric NOT NULL,
  fbco_msg text NOT NULL,
  fbco_fecha date NOT NULL,
  fbco_lkcnt numeric NOT NULL DEFAULT 0,
  fbco_idc character varying(100) NOT NULL,
  CONSTRAINT "Fbcoment_pkey" PRIMARY KEY (fbco_idc),
  CONSTRAINT "FK_FBCONTENT_CONTENIDO" FOREIGN KEY (fbco_contenido)
      REFERENCES contenido (cont_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

´´´
De manera asincrona se puede obtener los comentarios de facebook de acuerdo a la liga de donde se guardó la información. Los comentarios son guardados en lla tabla fbcoment. 


###USO:

##### SCRAP
´´´
$ java -jar jmc.jar scrap <rango de paginas (ejemplo:0 101)> <clase implementada (ejemplo:jmc.proc.JmcMMScrap)>
´´´
##### Obtencion comentarios Facebook
´´´
$ java -jar jmc.jar act-fbc <id del sitio soportado (ejemplo: 1)> <numero de comentarios (ejemplo:500)>
´´´

### Archivos de configuración.

##### props_config.properties
´´´
navegador='Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11'
archive_html=<directorio donde se guardaran las paginas obtenidas>
archive_json=<directorio donde se guardaran los archivos JSON de las conversaciones de facebook>
jmc.proc.JmcMMScrap=<liga de sitio soportado a obtener>
jmc.proc.JmcCBScrap=<liga de sitio soportado a obtener>

´´´
##### props_dao.properties
´´´
host=<host del gestor de base de datos>
puerto=<puerto del gestor de base de datos por default 5432>
sid=<nombre de la base de datos>
usuario=<usuario de conexión a la base de datos>
password=<ontraseña del usuario de la base de datos>
´´´


### Requisitos:
- Java versión: jdk7 o jdk8 (http://www.oracle.com/technetwork/java/javase/downloads/index.html)
- jsoup (http://jsoup.org/).
- json (https://code.google.com/p/json-simple/).
- JDBC PostgreSQL (https://jdbc.postgresql.org/).

### Autores:
- Miguel Angel Cedeño Garcidueñas &nbsp;&nbsp;  miguecg[at]gmail.com
- Jesse Williams Madriz Chavez &nbsp;&nbsp;	    madrizjesse[at]gmail.com
- José Gerardo Moreno García &nbsp;&nbsp;        mgtigmoreno[at]gmail.com 


 
