INSERT INTO PUBLIC.CELLAR(ID, CAPACITY, USER_ID) VALUES (1001, 27, 3);

INSERT INTO PUBLIC.WINE(ID, NAME, APPELLATION, PRODUCER, CREATOR_ID, REGION_ID, COLOR_ID) VALUES(1001, STRINGDECODE('Cuv\u00e9e les ruffes'), 'AOP terrasses du Larzac', STRINGDECODE('Ch\u00e2teau la sauvageonne'), 3, 9, 2);
INSERT INTO PUBLIC.WINE(ID, NAME, APPELLATION, PRODUCER, CREATOR_ID, REGION_ID, COLOR_ID) VALUES(1002, 'Domaine de la clotte', 'AOP Saint-Emilion', 'Vigneron et traditions', 3, 3, 2);
INSERT INTO PUBLIC.WINE(ID, NAME, APPELLATION, PRODUCER, CREATOR_ID, REGION_ID, COLOR_ID) VALUES(1003, STRINGDECODE('Calice ch\u00e2teau billeron bouquey'), 'AOP Saint-Emilion', 'SCEA Robin-Lafugie', 3, 3, 2);
INSERT INTO PUBLIC.WINE(ID, NAME, APPELLATION, PRODUCER, CREATOR_ID, REGION_ID, COLOR_ID) VALUES(1004, 'Coteaux du layon St-Aubin', 'AOP coteaux du layon', 'Vignoble de la Fresnaye', 3, 14, 1);
INSERT INTO PUBLIC.WINE(ID, NAME, APPELLATION, PRODUCER, CREATOR_ID, REGION_ID, COLOR_ID) VALUES (1005, 'Les Darons', 'AOP Languedoc', 'Jeff Carrel', 3, 9, 2);
INSERT INTO PUBLIC.WINE(ID, NAME, APPELLATION, PRODUCER, CREATOR_ID, REGION_ID, COLOR_ID) VALUES (1006, 'Cuvée Anaïs', 'AOP Touraine Mesland', 'Domaine de Rabelais', 3, 14, 2);

INSERT INTO PUBLIC.VINTAGE(ID, BARE_CODE, JHI_YEAR, WINE_ID) VALUES(1051, NULL, 2002, 1001);
INSERT INTO PUBLIC.VINTAGE(ID, BARE_CODE, JHI_YEAR, WINE_ID) VALUES(1052, NULL, 2017, 1002);
INSERT INTO PUBLIC.VINTAGE(ID, BARE_CODE, JHI_YEAR, WINE_ID) VALUES(1053, NULL, 2010, 1003);
INSERT INTO PUBLIC.VINTAGE(ID, BARE_CODE, JHI_YEAR, WINE_ID) VALUES(1054, NULL, 2015, 1004);
INSERT INTO PUBLIC.VINTAGE(ID, BARE_CODE, JHI_YEAR, WINE_ID) VALUES (1055, NULL, 2015, 1005);
INSERT INTO PUBLIC.VINTAGE(ID, BARE_CODE, JHI_YEAR, WINE_ID) VALUES (1056, NULL, 2014, 1006);

INSERT INTO PUBLIC.WINE_IN_CELLAR(ID, MIN_KEEP, MAX_KEEP, PRICE, QUANTITY, COMMENTS, CELLAR_ID, VINTAGE_ID) VALUES(1101, 3, 8, 6.0, 1, NULL, 1001, 1051);
INSERT INTO PUBLIC.WINE_IN_CELLAR(ID, MIN_KEEP, MAX_KEEP, PRICE, QUANTITY, COMMENTS, CELLAR_ID, VINTAGE_ID) VALUES(1102, 2, 10, NULL, 1, STRINGDECODE('Francois Ma\u00eetre'), 1001, 1052);
INSERT INTO PUBLIC.WINE_IN_CELLAR(ID, MIN_KEEP, MAX_KEEP, PRICE, QUANTITY, COMMENTS, CELLAR_ID, VINTAGE_ID) VALUES(1103, 2, 10, NULL, 1, NULL, 1001, 1053);
INSERT INTO PUBLIC.WINE_IN_CELLAR(ID, MIN_KEEP, MAX_KEEP, PRICE, QUANTITY, COMMENTS, CELLAR_ID, VINTAGE_ID) VALUES(1104, 1, 4, NULL, 1, 'Stephane', 1001, 1054);
