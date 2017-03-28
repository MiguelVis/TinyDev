' -----------------------------------
' TinyDev: Tic Tac Toe (Tres en Raya)
' -----------------------------------

' Variables
'

variable number ficha_lado = 96  ' Ancho / alto de la ficha en pixels

variable image ficha_x = "tic_tac_toe_x.png"
variable image ficha_o = "tic_tac_toe_o.png"
variable image ficha   = "tic_tac_toe_empty.png"

variable number tablero[9]

variable number casilla
variable number valor

variable number x
variable number y

variable String boton

variable number fin

variable number turno
variable number turnoJugador

variable string mensaje

' Inicialización
'

screen 3 * ficha_lado, 3 * ficha_lado

random 8

' Jugar partida
'
do
	set fin = 0
	set turno = 0

	call NuevoTablero

	if random() % 2 != 0 then
		set turnoJugador = 0
		set mensaje = "Empiezo yo."
	else
		set turnoJugador = 1
		set mensaje = "Empiezas tú."
	end if

	dialog information mensaje

	do
		if turnoJugador == 1 then call TurnoJugador
		else call TurnoOrdenador

		call CompruebaFin

		set turno = turno + 1
		set turnoJugador = 1 - turnoJugador
	loop until fin != 0 || turno == 9

	if fin == 1 then set mensaje = "¡Tú ganas!"
	else if fin == 2 then set mensaje = "¡Yo gano!"
	else set mensaje = "¡Tablas!"

	dialog yesNo mensaje + " ¿Otra partida?"

loop while reply() == "yes"

' Fin
'
	end

' Turno jugador
'
TurnoJugador:
	do
		do
			set boton = mouseClick()
		loop until boton == "left"

		set x = mouseX() / ficha_lado
		set y = mouseY() / ficha_lado
		set casilla = y * 3 + x
	loop until tablero[casilla] == 0

	set tablero[casilla] = 1
	call DibujaCasilla
	return

' Turno ordenador
'
TurnoOrdenador:
	do
		set casilla = random()
	loop while tablero[casilla] != 0

	set tablero[casilla] = 2
	call DibujaCasilla
	return

' Comprobar si la partida ha terminado
'
CompruebaFin:
	' X--
	' ---
	' ---

	if tablero[0] != 0 then

		' XXX
		' ---
		' ---

		if tablero[0] == tablero[1] && tablero[0] == tablero[2] then
			set fin = tablero[0]
			return
		end if

		' X--
		' X--
		' X--

		if tablero[0] == tablero[3] && tablero[0] == tablero[6] then
			set fin = tablero[0]
			return
		end if

		' X--
		' -X-
		' --X

		if tablero[0] == tablero[4] && tablero[0] == tablero[8] then
			set fin = tablero[0]
			return
		end if
	end if

	' --X
	' ---
	' ---

	if tablero[2] != 0 then

		' --X
		' --X
		' --X

		if tablero[2] == tablero[5] && tablero[2] == tablero[8] then
			set fin = tablero[2]
			return
		end if

		' --X
		' -X-
		' X--

		if tablero[2] == tablero[4] && tablero[2] == tablero[6] then
			set fin = tablero[2]
			return
		end if
	end if

	' -X-
	' -X-
	' -X-

	if tablero[1] != 0 then
		if tablero[1] == tablero[4] && tablero[1] == tablero[7] then
			set fin = tablero[1]
			return
		end if
	end if

	' ---
	' XXX
	' ---

	if tablero[3] != 0 then
		if tablero[3] == tablero[4] && tablero[3] == tablero[5] then
			set fin = tablero[3]
			return
		end if
	end if

	' ---
	' ---
	' XXX

	if tablero[6] != 0 then
		if tablero[6] == tablero[7] && tablero[6] == tablero[8] then
			set fin = tablero[6]
			return
		end if
	end if

	return

' Inicializar tablero y dibujarlo
'
NuevoTablero:
	set casilla = 0

	do
		set tablero[casilla] = 0
		call DibujaCasilla
		set casilla = casilla + 1
	loop until casilla == 9
	return

' Dibujar casilla
'
' Entrada: casilla
'
DibujaCasilla:
	set x = (casilla % 3) * ficha_lado
	set y = (casilla / 3) * ficha_lado

	set valor = tablero[casilla]

	if valor == 0 then image x, y, ficha
	else if valor == 1 then image x, y, ficha_x
	else image x, y, ficha_o

	return
