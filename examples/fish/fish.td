' ---------------
' TinyDev: Pecera
' ---------------

' Colores
'
variable string black = "black"
variable string blue = "blue"
variable string cyan = "cyan"
variable string gray = "gray"
variable string magenta = "magenta"
variable string orange = "orange"
variable string pink = "pink"
variable string red = "red"
variable string white = "white"
variable string yellow = "yellow"
variable string green = "green"

' Variables
'
variable number fish

variable number fishX[2]
variable number fishY[2]
variable string fishDir[2]
variable image fishLeft[2]
variable image fishRight[2]

variable number seaHorseX = 650
variable number seaHorseY = 320
variable string seaHorseDir = "down"
variable image seaHorse = "caballito_mar.png"

variable number tempX
variable number tempY

' Inicializar los peces
'
set fishX[0] = 350
set fishY[0] = 50
set fishDir[0] = "right"
set fishLeft[0] = "fish_red_left.png"
set fishRight[0] = "fish_red_right.png"

set fishX[1] = 350
set fishY[1] = 190
set fishDir[1] = "left"
set fishLeft[1] = "fish_green_left.png"
set fishRight[1] = "fish_green_right.png"

' Inicializar la pantalla
'
screen 800, 600
paper blue
clear

image 0, 350, "alga_marina.png"
image 300, 350, "estrella_mar.png"

' Bucle principal
'
MainLoop:
	' Mover el pez #0
	'
	set fish = 0
	call MoveFish

	' Mover el pez #1
	'
	set fish = 1
	call MoveFish

	' Mover el caballito de mar
	'
	call MoveSeaHorse

	' Una pequeña pausa
	'
	wait 30

	' Vuelta a empezar
	'
	jump MainLoop

' Mover un pez
'
' Parámetro de entrada: fish
'
MoveFish:
	pen blue
	if fishDir[fish] == "right" then call MoveFishRight
	else call MoveFishLeft
	if fishDir[fish] == "right" then image fishX[fish], fishY[Fish], fishRight[fish]
	else image fishX[fish], fishY[Fish], fishLeft[fish]
	return

' Mover el pez hacia la izquierda
'
MoveFishLeft:
	if fishX[fish] == 0 then
		set fishDir[fish] = "right"
	else
		set tempY = fishY[fish] + 104
		set tempX = fishX[fish] + 127
		line tempX, fishY[fish], tempX, tempY
		set fishX[fish] = fishX[fish] - 1
	end if
	return

' Mover el pez hacia la derecha
'
MoveFishRight:
	if fishX[fish] == 650 then
		set fishDir[fish] = "left"
	else
		set tempY = fishY[fish] + 104
		line fishX[fish], fishY[fish], fishX[fish], tempY
		set fishX[fish] = fishX[fish] + 1
	end if
	return

' Mover el caballito de mar
'
MoveSeaHorse:
	pen blue
	if seaHorseDir == "down" then call MoveSeaHorseDown
	else call MoveSeaHorseUp
	image seaHorseX, seaHorseY, seaHorse
	return

' Mover el caballito de mar hacia arriba
'
MoveSeaHorseUp:
	if seaHorseY == 320 then
		set seaHorseDir = "down"
	else
		set tempY = seaHorseY + 168
		set tempX = seaHorseX + 96
		line seaHorseX, tempY, tempX, tempY
		set seaHorseY = seaHorseY - 1
	end if
	return

' Mover el caballito de mar hacia abajo
'
MoveSeaHorseDown:
	if seaHorseY == 420 then
		set seaHorseDir = "up"
	else
		set tempX = seaHorseX + 96
		line seaHorseX, seaHorseY, seaHorseX + 96, seaHorseY
		set seaHorseY = seaHorseY + 1
	end if
	return
