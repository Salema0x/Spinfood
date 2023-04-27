<h1> To-Do-List </h1>

<h2> Bis zum 04.05.2023 </h2>

<ul>
<li>Für JUnit Tests können den .csv Teil testen, wo nur csv Dateien eingelesen werden müssen </li>
<li>den GUI Teil sollte bisschen bearbeitet werden indem man die Liste direkt angezeigt sieht, sobald man die Datei auswählt,statt wieder auf " Liste anzeigen " drucken zu müssen</li>
	<li> Bevor mit der Implementierung angefangen wird, Testfälle spezifizieren</li>
	<li> Datenstruktur zur Repräsentation von Teilnehmern implementieren <i>(wir sollten vielleicht nochmal darüber nachdenken welche Datenstruktur sich anbieten würde, wahrscheinlich sind Listen nicht das effizienteste)</i></li>
	<ul>
		<li>Vielleicht bietet sich hier eine Hash-Tabelle an, weil jeder Teilnehmer ja schon mit einer ID kommt.</li>
	</ul>
	<li> Implementierung Einlesen der Teilnehmer</li>
	<ul>
		<li>.csv Datei soll eingelesen werden, die einzelnen Teilnehmer werden in einer Datenstruktur gespeichert, die einzelnen Teilnehmer werden auf der Konsole ausgegeben</li>
	</ul>
	<li> Klassendiagramm anpassen an die neuen Anforderungen</li>
	<li> Gantt-Diagramm mit Planung für Woche 2 erweitern <i>(Speicherung in einer neuen Datei)</i></li>
	<li> Testdokumentation erstellen <i>(jeder Test usw. muss dokumentiert werden)</i></li>
</ul>

<h3> Gedanken Implementierung Algorithmus </h3>

<ul>
<li> Es gibt eine extra Klasse zum einlesen von der .csv Datei "ParticipantFactory" die Main sollte einfach nur die verschiedenen Methoden aus den unterschiedlichen Klassen aufrufen. Das macht es einfacher aus der Main heraus den gesamten Ablauf zu erkennen.</li>
<li> Eine GUI in der der Pfad zu einer .csv Datei angegeben werden kann </li>
<li> Programm nimmt diesen Pfad entgegen und erstellt aus der .csv Datei Teilnehmer</li>
<li> angemeldete Paare werden als Pairs abgespeichert und als Teilnehmer damit wirklich erstmal alle Teilnehmer einzeln angezeigt werden können </li>
<li> Es muss sichergestellt werden, dass wirklich alle Einträge abgearbeitet werden </li>
<li> Wenn eine leere .csv Datei angegeben wird darf nichts unerwartetes passieren: Ausgabe auf der Konsole: kein Teilnehmer hat sich angemeldet</li>
<li> Für die Küchen sollte es meiner Meinung nach wirklich eine Kitchen Klasse geben</li>
<li> Wir brauchen noch eine Methode um die agerange zu berechnen</li>
<li> Wir brauchen noch eine Methode um count_wg zu berechnen</li>
<li> Schönerer Code: einlesen der Datei mittels Stream API, und erstellen eines participants mittels konstruktor und nicht mit so vielen set aufrufen, ist zwar komplizierter aber sehr viel schöner. Können wir uns ja mal merken falls wir noch Zeit haben.</li>
<li> Macht die Hash-Tabelle so bis jetzt Sinn? Bis jetzt brauchen wir die noch nicht wirklich, wird aber später evtl. nützlich sein. Eigentlich ist ja eine Hash-Tabelle nur effizient wenn wir sie mit einer Hash-Funktion verwenden? Hier können wir aber vielleicht die Participants mittels der ID einfacher suchen, also ist das vielleicht schon sinnvoll insbesondere beim Löschen eines Teilnehmers</li>
<li> Unit Test für die Methode zum .csv Datei einlesen </li>
<ul>
	<li>Liste wird übergeben -> Es wird eine Liste an Teilnehmern auf der Konsole ausgegeben</li>
	<li>Paare werden korrekt aufgesplittet in zwei Teilnehmer (wahrscheinlich extrahieren in eine zweite methode)</li>
</ul>
<li> Unit Tests für die anderen Methoden zur Berechnung von wg_count und agerange</li>
</ul>

<h2> :heavy_check_mark: Bis zum 20.04.2023 </h2>

<ul>
	<li> :heavy_check_mark:<del>User "taentzer" als Contributer hinzufügen <i>(Bis jetzt gab es kein Konto mit dem Usernamen "taentzer")</i></del></li>
	<li> :heavy_check_mark:<del>Abdelrahman als Contributer hinzufügen</del></li>
	<li> :heavy_check_mark:<del>Lennart als Contributer hinzufügen</del></li>
	<li> :heavy_check_mark:<del>Sicherstellen, dass alle ein korrektes Setup (IntelliJ, git, JDK, Java Projekt) haben <i>(Siehe Abschnitt <a name="Set-Up">Set-Up</a>)</i></del></li>
	<ul>
		<li> <del>David Krell</del> </li>
		<li> <del>David Klitsch </del> </li>
		<li> <del>Abdelrahman Salem</del> </li>
		<li> <del>Lennart Scheil<del> </li>
	</ul>
	<li>:heavy_check_mark:<del>Alle haben die Anforderungsbeschreibung gelesen</del></li>
	<ul>
		<li> <del>David Krell</del> </li>
		<li> <del>David Klitsch </del></li>
		<li> <del>Abdelrahman Salem</del> </li>
		<li> <del>Lennart Scheil<del> </li>
	</ul>
	<li> :heavy_check_mark: <del>Vorläufige Paketstruktur erstellen</del> </li>
	<li> :heavy_check_mark: <del> Entscheiden welches Build-Tool genutzt werden soll</del> <i> (Es wurde sich für Maven entschieden)</i></li>
	<li> :heavy_check_mark: <del> Ordner Dokumentation im Repository erstellen</del> </li>
	<li> :heavy_check_mark:<del><a href="https://de.wikipedia.org/wiki/Gantt-Diagramm">Gantt-Diagramm</a> für den Zeitplan und Arbeitseinteilung erstellen </del></li>
	<li> :heavy_check_mark:<del>Vorläufiges Klassendiagramm erstellen </del></li>
	</ul>
<h3>Set-Up</h3>
<p>Am besten ist es, wenn wir alle auf den gleichen Versionen anfangen zu entwickeln um so potentielle Konflikte durch verschiedene Versionen zu umgehen.</p>
<h4>Git Updaten und einrichten </h4>
Wir werden die neuste git-Version verwenden, dazu sollte jeder sein git einmal updaten.
Unter Windows öffnet ihr eine git Bash und gibt <code>git update-git-for-windows</code> ein.  Falls das nicht funktioniert könnt ihr zuerst <code>git-update</code> ausprobieren und dann nochmal <code>git update-git-for-windows</code>. Wenn dann "Up to date" ausgegeben wird habt ihr die neuste Version.
Für Linux und Mac kann man <a href="https://phoenixnap.com/kb/how-to-update-git">hier</a> nachlesen.

Achtet auch darauf Git richtig zu konfigurieren mit den folgenden Befehlen: <code> git config --global user.name "euer git Username (hier Ilias Kürzel)"</code> und <code> git config --global user.email "eure students mail"</code>.
<h4>JDK Version</h4>
Ich habe mich für die JDK Version 17 entschieden, obwohl auch schon JDK 20 draußen ist, ist das JDK 17 der letzte Long-Term JDK Release und kriegt Updates bis September 2024. Wohingegen die neuere Version JDK 20 nur Updates bis September 2023 erhält. Ihr könnt das JDK 17 <a href="https://www.oracle.com/java/technologies/downloads/#jdk17-windows">hier</a> herunterladen.
<h4>IntelliJ Version</h4>
Am besten ist es vermutlich wenn ihr euch alle die Ultimate Version von IntelliJ holt. Dafür könnt ihr euch auf der Website von <a href="https://jetbrains.com">Jetbrains</a> ein Konto mit eurer Students Mail Adresse anlegen und dann erhaltet ihr die Lizenz ein Jahr lang kostenlos. Wenn ihr schon die Ultimate Version habt, dann ladet euch am besten die neuste Version 2023.1 herunter. Dafür klickt ihr in IntelliJ auf "Help" und dann auf "Check for Updates".
<h4>Java Projekt Setup</h4>
Um das Projekt in IntelliJ verwenden zu können und um auch von IntelliJ aus committen und pushen zu können, klont ihr euch zuerst das git Repository an einen Ort eurer Wahl. Dann klickt ihr auf den Ordner "Spinfood-Projekt" und öffnet diesen Ordner mit IntelliJ.
Ihr könnt dann im IntelliJ den Code verändern und dort mittels "Git" -> "commit". Eure Änderungen committen und pushen. Am besten pullt ihr immer mittels "Git" -> "pull" bevor ihr anfangt weiter zu programmieren.
<br>
<br>
<p>Note: Falls ihr eine Fehlermeldung beim Klonen habt die besagt <code> fatal: credential-cache unavailable; no unix socket support</code>, benutzt diesen Befehl <code> git config --global credential.helper wincred</code> dann diesen <code> git config --global credential.helper manager-core </code>.</p>
