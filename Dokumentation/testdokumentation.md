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

    |--------------------------------------------------------------|
	| Methode                      | #Tests | #Fehler | Voll. Abd. |
	|------------------------------+--------+---------+------------|
	| ParticipantFactory.readCSV() | 9      | 0       | Nein       |
	|--------------------------------------------------------------|
	
	