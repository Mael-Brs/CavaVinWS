INSERT INTO PUBLIC.COLOR(ID, COLOR_NAME) VALUES
(1, 'Blanc'),
(2, 'Rouge'),
(3, 'Rose');

INSERT INTO PUBLIC.REGION(ID, REGION_NAME) VALUES
(1, 'Alsace'),
(2, 'Beaujolais'),
(3, 'Bordeaux'),
(4, 'Bourgogne'),
(5, 'Champagne'),
(6, 'Poitou-Charentes'),
(7, 'Corse'),
(8, 'Jura'),
(9, 'Languedoc'),
(10, 'Provence'),
(11, 'Roussillon'),
(12, 'Savoie'),
(13, 'Sud-Ouest'),
(14, 'Vallée de la Loire'),
(15, 'Vallée du Rhône');

INSERT INTO PUBLIC.WINE_AGING_DATA(ID, MIN_KEEP, MAX_KEEP, COLOR_ID, REGION_ID) VALUES
(51, 1, 6, 2, 14),
(52, 2, 10, 2, 3),
(53, 2, 10, 2, 13),
(54, 2, 8, 2, 9),
(55, 2, 8, 2, 10),
(56, 1, 6, 2, 15),
(57, 1, 4, 2, 2),
(58, 2, 10, 2, 4),
(59, 2, 8, 2, 11),
(60, 1, 4, 1, 14),
(61, 2, 8, 1, 3),
(62, 1, 5, 1, 13),
(63, 1, 3, 1, 9),
(64, 1, 3, 1, 10),
(65, 1, 3, 1, 11),
(66, 1, 4, 1, 15),
(67, 1, 5, 1, 4),
(68, 4, 20, 1, 8),
(69, 1, 5, 1, 1),
(70, 1, 3, 1, 5),
(71, 1, 4, 3, 9),
(72, 1, 3, 3, 10),
(73, 0, 2, 3, 15),
(74, 0, 1, 3, 13);
