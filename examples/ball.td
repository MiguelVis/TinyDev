' -------------
' TinyDev: Ball
' -------------

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
variable number BoardWidth   = 400
variable number BoardHeight  = 400
variable number BallDiameter = 32
variable number PosX
variable number PosY
variable number OldX
variable number OldY
variable number LastPosX
variable number LastPosY
variable number MoveX = 1
variable number MoveY = 1

' Inicialización del entorno
'
random 32767

' Calcular posiciones máximas
'
set LastPosX = BoardWidth - BallDiameter
set LastPosY = BoardHeight - BallDiameter

' Calcular la posición inicial
'
set PosX = random() * LastPosX / 32767
set PosY = random() * LastPosY / 32767

' Inicializar pantalla
'
screen BoardWidth, BoardHeight
paper black
clear

' Dibujar la pelota en un bucle infinito
'
	do
		pen gray
		paper red
		ellipse fill PosX, PosY, BallDiameter, BallDiameter

		wait 125

		' Calcular la nueva posición de la pelota
		'
		set OldX = PosX
		set OldY = PosY

		set PosX = PosX + MoveX * BallDiameter

		if PosX < 0 then call MoveToRight
		else if PosX > LastPosX then call MoveToLeft

		set PosY = PosY + MoveY * BallDiameter

		if PosY < 0 then call MoveToDown
		else if PosY > LastPosY then call MoveToUp

		' Borrar la pelota
		'
		pen black
		ellipse solid OldX, OldY, BallDiameter, BallDiameter

		' Vuelta a empezar
		'
	loop while 1 == 1

' Mover la pelota hacia la derecha
'
MoveToRight:
	set PosX = 0
	set MoveX = 1
	call AdjustVert
	return

' Mover la pelota hacia la izquierda
'
MoveToLeft:
	set PosX = LastPosX
	set MoveX = -1
	call AdjustVert
	return

' Cambiar el movimiento vertical
'
AdjustVert:
	set MoveY = random()
	if MoveY < 11000 then set MoveY = 0
	else if MoveY < 21000 then set MoveY = -1
	else set MoveY = 1
	return

' Mover la pelota hacia arriba
'
MoveToUp:
	set PosY = LastPosY
	set MoveY = -1
	call AdjustHoriz
	return

' Mover la pelota hacia abajo
'
MoveToDown:
	set PosY = 0
	set MoveY = 1
	call AdjustHoriz
	return

' Cambiar el movimiento horizontal
'
AdjustHoriz:
	set MoveX = random()
	if MoveX < 11000 then set MoveX = 0
	else if MoveX < 21000 then set MoveX = -1
	else set MoveX = 1
	return
