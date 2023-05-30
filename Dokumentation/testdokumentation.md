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
	| ParticipantFactory.readCSV() | 11     | 0       | Nein       |
	|--------------------------------------------------------------|




Test Pärchenbildung:

	- testen ob Participants mehrfach einem Pärchen zugeordnet wurden
	- testen ob ein Pärchen mit nicht passenden Essensvorlieben generiert wurde
	- testen ob ein Pärchen ohne Küche generiert wurde
	- testen ob FoodKennzahl für jedes Pärchen richtig berechnet wurde
	- testen ob AgeKennzahl für jedes Pärchen richtig berechnet wurde
	- testen ob SexKennzahl für jedes Pärchen richtig berechnet wurde	

    	|--------------------------------------------------------------|
	| Methode                      | #Tests | #Fehler | Voll. Abd. |
	|------------------------------+--------+---------+------------|
	| PairListFactory 	       |   6	| 0       | Nein       |
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
	| 	createGroups	       |   7	| 0       | Nein       |
	|--------------------------------------------------------------|	
	

Test Absagenhandeling