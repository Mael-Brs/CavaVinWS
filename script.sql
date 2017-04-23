;             
CREATE USER IF NOT EXISTS CAVAVIN SALT '78a03aca24589e88' HASH '5a834ec7cb9c22f2eaf7b236e81cb6d9df5fb70906e18e4d09d7dab30d80ba95' ADMIN;      
CREATE SEQUENCE PUBLIC.HIBERNATE_SEQUENCE START WITH 1300 INCREMENT BY 50;    
CREATE CACHED TABLE PUBLIC.WINE_AGING_DATA(
    ID BIGINT NOT NULL,
    MIN_KEEP INT,
    MAX_KEEP INT,
    COLOR_ID BIGINT,
    REGION_ID BIGINT
);    
ALTER TABLE PUBLIC.WINE_AGING_DATA ADD CONSTRAINT PUBLIC.PK_WINE_AGING_DATA PRIMARY KEY(ID);  
-- 23 +/- SELECT COUNT(*) FROM PUBLIC.WINE_AGING_DATA;        
INSERT INTO PUBLIC.WINE_AGING_DATA(ID, MIN_KEEP, MAX_KEEP, COLOR_ID, REGION_ID) VALUES
(1201, 1, 6, 2, 14),
(1202, 2, 10, 2, 3),
(1203, 2, 10, 2, 13),
(1204, 2, 8, 2, 9),
(1205, 2, 8, 2, 10),
(1206, 1, 6, 2, 15),
(1207, 1, 4, 2, 2),
(1208, 2, 10, 2, 4),
(1209, 2, 8, 2, 11),
(1210, 1, 4, 1, 14),
(1211, 2, 8, 1, 3),
(1212, 1, 5, 1, 13),
(1213, 1, 3, 1, 9),
(1214, 1, 3, 1, 10),
(1215, 1, 3, 1, 11),
(1216, 1, 4, 1, 15),
(1217, 1, 5, 1, 4),
(1218, 4, 20, 1, 8),
(1219, 1, 5, 1, 1),
(1220, 1, 3, 1, 5),
(1221, 1, 4, 3, 9),
(1222, 1, 3, 3, 10),
(1223, 0, 2, 3, 15);     
ALTER TABLE PUBLIC.WINE_AGING_DATA ADD CONSTRAINT PUBLIC.FK_WINE_AGING_DATA_REGION_ID FOREIGN KEY(REGION_ID) REFERENCES PUBLIC.REGION(ID) NOCHECK;            
ALTER TABLE PUBLIC.WINE_AGING_DATA ADD CONSTRAINT PUBLIC.FK_WINE_AGING_DATA_COLOR_ID FOREIGN KEY(COLOR_ID) REFERENCES PUBLIC.COLOR(ID) NOCHECK;               
