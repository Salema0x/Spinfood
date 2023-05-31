Test Einlesen von Teilnehmern

	- testen ob Teilnehmer korrekt eingelesen werden
	- testen ob, wenn .csv Datei leer, auch kein Teilnehmer eingelesen wird und es zu keinem Fehler kommt
	- wenn der selbe Teilnehmer zweimal in der List ist, wird er nur einmal angezeigt
	- testen wenn ein Paar in der Liste ist werden die Teilnehmer aus dem Paar extrahiert und beide Teilnehmer auf der Liste angezeigt
	- testen ob die Teilnehmer immer noch korrekt eingelesen werden, wenn Kitchen_Story leer ist
	- testen ob die Teilnehmer immer noch korrekt eingelesen werden, wenn Kitchen_story leer ist umd koordinaten leer sind 
	- testen ob die Teilnehmer immer noch korrekt eingelesen werden, wenn kitchen_story eingetragen ist und koordinaten leer sind
	- testen ob die Teilnehmer immer noch korrekt eingelesen werden, wenn es ein Paar ist und kitchen_Story leer ist
	- testen ob die Teilnehmer immer noch korrekt eingelesen werden, wenn es ein Paar ist und kitchen_Story leer ist und Koordinaten leer sind
	- testen ob Teilnehmer korrekt als Succsessor markiert werden, wenn die Anmeldungen voll sind
	- testen ob Teilnehmer korrekt als Successor markiert werden, wenn die angegebene Küche schon von zu vielen WG mitgliedern genutz wird

    	|--------------------------------------------------------------|
	| Methode                      | #Tests | #Fehler | Voll. Abd. |
	|------------------------------+--------+---------+------------|
	| ParticipantFactory.readCSV() | 11     | 0       | Ja	       |
	|--------------------------------------------------------------|


Test Paarlisten
	
	- testen ob die Altersdifferenz eines Pärchen richtig berechnet wird
	- testen ob die Altersdifferenz einer Paarliste richtig berechnet wird 
	- testen ob die Geschlechterdiversität eines Pärchen richtig berechnet wird
	- testen ob die Geschlechterdiversität einer Paarliste richtig berechnet wird
	- testen ob die richtige Anzahl an Pärchen generiert wurde
	- testen ob die Essensvorliebenkennzahl eines Pärchens richtig berechnet wird	
	- testen ob die Essensvorliebenkennzahl einer Paarliste richtig berechnet wird
	- testen ob die Anzahl an Nachvolgern richtig berechnet wird
	|--------------------------------------------------------------|
	| Methode                      | #Tests | #Fehler | Voll. Abd. |
	|------------------------------+--------+---------+------------|
	| PairListFactory()            | 8	| 0       | Ja	       |
	|--------------------------------------------------------------|

Test Pärchenbildung:

	- testen ob Participants mehrfach einem Pärchen zugeordnet wurden
	- testen ob Pärchen mit nicht passenden Essensvorlieben generiert wurde
	- testen ob keine weiteren Pärchen (mit Participants aus WG) erstellt werden, wenn schon drei Pärchen mit participants aus derselben WG erstellt wurden
	- testen ob Pärchen ohne Küche generiert wurden
	- testen ob Pärchen trotz hohen Eigenschaftsdifferenzen (z.B. Altersunterschied) generiert werden
	- testen ob die Alterskennszahl der Pärchen richtig generiert wird
	- testen ob die Essensvorliebenkennzahl der Pärchen richtig generiert wird
	- testen ob die Diversitätskennzahl eines Pärchens richtig generiert wird

    	|--------------------------------------------------------------|
	| Methode                      | #Tests | #Fehler | Voll. Abd. |
	|------------------------------+--------+---------+------------|
	| PairListFactory 	       |   8	| 0       | Ja	       |
	|--------------------------------------------------------------|	



	
Test Gruppenbildung:

	-testen ob kein Pärchen mehreren Gruppen pro Gang zugeordnet wurde
	-testen ob Pärchen die schonmal zusammen in einer Gruppe waren, wieder in die gleiche Gruppe zugeordnet wurden
	-testen ob in den Gruppen Pärchen mit inkompatiblen Essensvorlieben sind
	-testen ob jedes Pärchen einmal kocht
	-testen ob Pärchen mehr als einmal kochten
	-testen ob in gemischten Gruppen (betreffend der Essensvorlieben) maximal ein Pärchen als Vorliebe none/meat hat
	-testen ob alle Pärchen eine Küche zur verfügung haben
	

	|--------------------------------------------------------------|
	| Methode                      | #Tests | #Fehler | Voll. Abd. |
	|------------------------------+--------+---------+------------|
	| createGroups()       	       |   7	| 0       | Nein       |
	|--------------------------------------------------------------|	
	

Test Absagenhandeling

	-testen ob Pärchen/Teilnehmer die abgesagt haben, korrekt aus den Gruppen entfernt wurden
	-testen ob Gruppen wieder aufgefüllt wurden
	-testen ob die neuen Pärchen/Gruppen legale Pärchen/Gruppen sind
	-testen ob die SuccsessorList die aufgefüllten Pärchen/Participants nicht mehr enthält

	|--------------------------------------------------------------|
	| Methode                      | #Tests | #Fehler | Voll. Abd. |
	|------------------------------+--------+---------+------------|
	| performAdjustment() 	       |   1	| 1	  | Nein       |
	|--------------------------------------------------------------|
	| updateGroupList() 	       |   1	| 1       | Nein       |
	|--------------------------------------------------------------|
	| updateWaitingList() 	       |   1 	| 1       | Nein       |
	|--------------------------------------------------------------|
	| completeGroups() 	       |   3	| 3       | Nein       |
	|--------------------------------------------------------------|
