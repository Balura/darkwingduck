INSERT INTO `threat_categories` VALUES (1,'B1','Elementare Gefährdungen'),(2,'B2','Höhere Gewalt'),(3,'B3','Organisatorische Mängel'),(4,'B4','Menschliche Fehlhandlungen'),(5,'B5','Technisches Versagen'),(6,'B6','Vorsätzliche Handlungen');
INSERT INTO `threats` VALUES (1, 1 , 0, 1, 'Ungünstige klimatische Bedingungen',  1),(2,0 , 0, 1,'Personalausfall',  2),(3, 1 , 0, 1,'Feuer',2),(4, 1 , 1, 0,'Fehlende oder unzureichende Regelungen',  3)
INSERT INTO `measures` VALUES (1, 'Brandmelder installieren', 3),(2, 'Roboter einsetzen', 2);
ALTER TABLE threats ALTER COLUMN availability SET DEFAULT 1;
ALTER TABLE threats ALTER COLUMN integrity SET DEFAULT 1;
ALTER TABLE threats ALTER COLUMN confidentiality SET DEFAULT 1;

INSERT INTO `availability` VALUES (1, 'Sehr hoch'),(2, 'Hoch'),(3, 'Mittel'),(4, 'Gering');
INSERT INTO `integrity` VALUES (1, 'Sehr hoch'),(2, 'Hoch'),(3, 'Mittel'),(4, 'Gering');
INSERT INTO `confidentiality` VALUES (1, 'Sehr hoch'),(2, 'Hoch'),(3, 'Mittel'),(4, 'Gering');
INSERT INTO `data_classification` VALUES (1, 'Geheim'),(2, 'Vertraulich'),(3, 'Intern'),(4, 'Öffentlich');
INSERT INTO `applications` VALUES (1, 'SAP',1,2,4,3);