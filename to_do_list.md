<h1> To-Do-List </h1>

<h2> Bis zum 16.04.2023 </h2>

<ul>
	<li> User "taentzer" als Contributer hinzufügen <i>(Bis jetzt gab es kein Konto mit dem Usernamen "taentzer")</i></li>
	<li> Zu Luc Wallis Kontakt aufnehmen und als Contributer hinzufügen <i>(Kontakt wurde via E-Mail aufgenommen, es wird auf Antwort gewartet.)</i></li>
	<li> :heavy_check_mark:<del>Abdelrahman als Contributer hinzufügen</del></li>
	<li> Sicherstellen, dass alle ein korrektes Setup (IntelliJ, git, JDK, Java Projekt) haben <i>(Siehe Abschnitt <a name="Set-Up">Set-Up</a>)</i></li>
	<ul>
		<li> David Krell </li>
		<li> David Klitsch </li>
		<li> Abdelrahman Salem </li>
		<li> Luc Wallis </li>
	</ul>
	<li> Alle haben die Anforderungsbeschreibung gelesen </li>
	<ul>
		<li> David Krell </li>
		<li> David Klitsch </li>
		<li> Abdelrahman Salem </li>
		<li> Luc Wallis </li>
	</ul>
	<li> Vorläufige Paketstruktur erstellen </li>
	<li> :heavy_check_mark: <del> Entscheiden welches Build-Tool genutzt werden soll</del> <i> (Es wurde sich für Maven entschieden)</i></li>
	<li> :heavy_check_mark: <del> Ordner Dokumentation im Repository erstellen</del> </li>
	<li> <a href="https://de.wikipedia.org/wiki/Gantt-Diagramm">Gantt-Diagramm</a> für den Zeitplan und Arbeitseinteilung erstellen </li>
	<li> Vorläufiges Klassendiagramm erstellen </li>
	</ul>
<h3>Set-Up</h3>
<p>Am besten ist es, wenn wir alle auf den gleichen Versionen anfangen zu entwickeln um so potentielle Konflikte durch verschiedene Versionen zu umgehen.</p>
<h4>Git Updaten </h4>
Wir werden die neuste git-Version verwenden, dazu sollte jeder sein git einmal updaten.
Unter Windows öffnet ihr eine git Bash und gibt <code>git update-git-for-windows</code> ein.  Falls das nicht funktioniert könnt ihr zuerst <code>git-update</code> ausprobieren und dann nochmal <code>git update-git-for-windows</code>. Wenn dann "Up to date" ausgegeben wird habt ihr die neuste Version.
Für Linux und Mac kann man <a href="https://phoenixnap.com/kb/how-to-update-git">hier</a> nachlesen.
<h4>JDK Version</h4>
Ich habe mich für die JDK Version 17 entschieden, obwohl auch schon JDK 20 draußen ist, ist das JDK 17 der letzte Long-Term JDK Release und kriegt Updates bis September 2024. Wohingegen die neuere Version JDK 20 nur Updates bis September 2023 erhält. Ihr könnt das JDK 17 <a href="https://www.oracle.com/java/technologies/downloads/#jdk17-windows">hier</a> herunterladen.
<h4>IntelliJ Version</h4>
Am besten ist es vermutlich wenn ihr euch alle die Ultimate Version von IntelliJ holt. Dafür könnt ihr euch auf der Website von <a href=www.jetbrains.com>Jetbrains</a> ein Konto mit eurer Students Mail Adresse anlegen und dann erhaltet ihr die Lizenz ein Jahr lang kostenlos. Wenn ihr schon die Ultimate Version habt, dann ladet euch am besten die neuste Version 2023.1 herunter. Dafür klickt ihr in IntelliJ auf "Help" und dann auf "Check for Updates".
<h4>Java Projekt Setup</h4>
Um das Projekt in IntelliJ verwenden zu können und um auch von IntelliJ aus committen und pushen zu können, klont ihr euch zuerst das git Repository an einen Ort eurer Wahl. Dann klickt ihr auf den Ordner "Spinfood-Projekt" und öffnet diesen Ordner mit IntelliJ.
Ihr könnt dann im IntelliJ den Code verändern und dort mittels "Git" -> "commit". Eure Änderungen committen und pushen. Am besten pullt ihr immer mittels "Git" -> "pull" bevor ihr anfangt weiter zu programmieren.