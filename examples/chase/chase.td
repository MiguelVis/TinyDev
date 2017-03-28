' --------------
' TinyDev: Chase
' --------------

' Notas:
'
' El alto y ancho del tablero han de ser igual (es cuadrado).
'
' El alto y ancho de las celdas han de ser igual (son cuadradas).

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
variable number FaceSidePixels = 40

variable number BoardSideCells = 12
variable number BoardSidePixels = BoardSideCells * FaceSidePixels
variable number BoardMaxSide    = BoardSidePixels - FaceSidePixels
variable image  BoardFace   = "chase_ground.png"

variable image  HeroFace     = "chase_hero.png"
variable image  HeroFaceDead = "chase_hero_dead.png"
variable image  HeroFaceWin  = "chase_hero_win.png"
variable number HeroLife
variable number HeroY
variable number HeroX
variable number HeroOldY
variable number HeroOldX
variable number HeroTeletr

variable image  MonsterFace     = "chase_robot.png"
variable image  MonsterFaceDead = "chase_robot_dead.png"
variable image  MonsterFaceWin  = "chase_robot_win.png"
variable number MonsterLife
variable number MonsterY
variable number MonsterX
variable number MonsterOldY
variable number MonsterOldX

variable image  WallFace = "chase_wall.png"
variable number Wall_1_Y =  2 * FaceSidePixels
variable number Wall_1_X =  2 * FaceSidePixels
variable number Wall_2_Y = (BoardSideCells - 3) * FaceSidePixels
variable number Wall_2_X = (BoardSideCells - 3) * FaceSidePixels

variable sound SoundStep     = "sound_step.wav"
variable sound SoundGameOver = "sound_game_over.wav"

variable string KeyPressed

variable number x
variable number y

' Initialización
'
random BoardSideCells - 1

' Presentación
'
screen BoardSidePixels, BoardSidePixels
paper black
clear

pen red
font 24, bold
print center "Robot Chase"
print break() + break()
font 16
print center "Un robot te persigue..."
print break() + break()
print center "¡Tu única opción es escapar!"
print break() + break()
print center "Utiliza las teclas del cursor para moverte,"
print break()
print center "y la barra espaciadora para"
print break()
print center "teletransportarte (1 sola vez)."
print break() + break()
print center "¡Buena suerte!"
print break() + break()
print center "Pulsa una tecla para empezar"

call ReadKey

paper white

' Comienzo de la partida
'
Start:
	set HeroLife = 1
	set HeroY = 5 * FaceSidePixels
	set HeroX = 5 * FaceSidePixels
	set HeroOldY = HeroY
	set HeroOldX = HeroX
	set HeroTeletr = 1

	set MonsterLife = 1
	call MonsterSetPos
	
	call PrintBoard
	call HeroUpdate
	call MonsterUpdate

' Bucle principal
'
MainLoop:
	' Mover al protagonista
	'
	call HeroPosition
	call HeroUpdate

	' Comprobar si le han cazado
	'
	if HeroLife == 0 then jump TheEnd

	' Mover al robot
	'
	call MonsterMove
	call MonsterUpdate
	call HeroUpdate

	' Comprobar si le han cazado
	'
	if HeroLife == 0 then jump TheEnd

	' Comprobar si el robot ha chocado
	'
	if MonsterLife == 0 then jump TheEnd

	' Sonido
	'
	sound SoundStep

	' Vuelta a empezar
	'
	jump MainLoop

' Fin de la partida
'
TheEnd:
	sound SoundGameOver

	dialog YesNo "¿Quieres jugar otra partida?"

	if reply() == "yes" then jump Start

	' Fin del juego
	'
	cursor to 0, 64
	font 64, bold, transparent
	print center "Game Over"

	end

' Imprimir la zona de juego
'
PrintBoard:
	clear

	set y = 0

	do
		set x = 0

		do
			image x, y, BoardFace
			set x = x + FaceSidePixels
		loop while x < BoardSidePixels

		set y = y + FaceSidePixels
	loop while y < BoardSidePixels

	image Wall_1_X, Wall_1_Y, WallFace
	image Wall_2_X, Wall_2_Y, WallFace

	return

' Actualizar robot
'
MonsterUpdate:
	image MonsterOldX, MonsterOldY, BoardFace

	if MonsterLife == 0 then image MonsterX, MonsterY, MonsterFaceDead
	else if HeroLife == 0 then image MonsterX, MonsterY, MonsterFaceWin
	else image MonsterX, MonsterY, MonsterFace

	set MonsterOldY = MonsterY
	set MonsterOldX = MonsterX
	return

' Actualizar protagonista
'
HeroUpdate:
	image HeroOldX, HeroOldY, BoardFace

	if HeroLife == 0 then image HeroX, HeroY, HeroFaceDead
	else if MonsterLife == 0 then image HeroX, HeroY, HeroFaceWin
	else image HeroX, HeroY, HeroFace

	set HeroOldY = HeroY
	set HeroOldX = HeroX
	return

' Mover al protagonista
'
HeroPosition:
	call ReadKey

	if KeyPressed == "up" then set HeroY = HeroY - FaceSidePixels
	else if KeyPressed == "down" then set HeroY = HeroY + FaceSidePixels
	else if KeyPressed == "left" then set HeroX = HeroX - FaceSidePixels
	else if KeyPressed == "right" then set HeroX = HeroX + FaceSidePixels
	else if KeyPressed == " " && HeroTeletr > 0 then jump HeroRandPos
	else jump HeroPosition

	if HeroY < 0 then set HeroY = 0
	else if HeroY > BoardMaxSide then set HeroY = BoardMaxSide

	if HeroX < 0 then set HeroX = 0
	else if HeroX > BoardMaxSide then set HeroX = BoardMaxSide

	' Comprobar si ha chocado con un muro
	'
	if HeroY == Wall_1_Y && HeroX == Wall_1_X then jump HeroPosOld
	if HeroY == Wall_2_Y && HeroX == Wall_2_X then jump HeroPosOld

	' Comprobar si ha chocado con el robot
	'
	if HeroY == MonsterY && HeroX == MonsterX then set HeroLife = 0
	else return

HeroPosOld:
	set HeroY = HeroOldY
	set HeroX = HeroOldX
	return

' Posicionar aleatoriamente al protagonista
'
HeroRandPos:
	set HeroY = random() * FaceSidePixels
	set HeroX = random() * FaceSidePixels

	if HeroY == HeroOldY && HeroX == HeroOldX then jump HeroRandPos
	if HeroY == MonsterY && HeroX == MonsterX then jump HeroRandPos
	if HeroY == Wall_1_Y && HeroX == Wall_1_X then jump HeroRandPos
	if HeroY == Wall_2_Y && HeroX == Wall_2_X then jump HeroRandPos

	set HeroTeletr = HeroTeletr - 1
	return

' Mover al robot
'
MonsterMove:
	if MonsterY < HeroY then set MonsterY = MonsterY + FaceSidePixels
	else if MonsterY > HeroY then set MonsterY = MonsterY - FaceSidePixels

	if MonsterX < HeroX then set MonsterX = MonsterX + FaceSidePixels
	else if MonsterX > HeroX then set MonsterX = MonsterX - FaceSidePixels

	' Comprobar si el robot ha chocado con un muro

	if MonsterY == Wall_1_Y && MonsterX == Wall_1_X then jump MonsterMoveDead
	if MonsterY == Wall_2_Y && MonsterX == Wall_2_X then jump MonsterMoveDead

	' Comprobar si ha chocado con el protagonista
	'
	if HeroY == MonsterY && HeroX == MonsterX then set HeroLife = 0
	else return

MonsterMoveOld:
	set MonsterY = MonsterOldY
	set MonsterX = MonsterOldX
	return

MonsterMoveDead:
	set MonsterLife = 0
	jump MonsterMoveOld

' Posicionar aleatoriamente al robot
'
MonsterSetPos:
	set MonsterY = random() * FaceSidePixels
	set MonsterX = random() * FaceSidePixels

	if MonsterY == HeroY && MonsterX == HeroX then jump MonsterSetPos
	if MonsterY == Wall_1_Y && MonsterX == Wall_1_X then jump MonsterSetPos
	if MonsterY == Wall_2_Y && MonsterX == Wall_2_X then jump MonsterSetPos

	set MonsterOldY = MonsterY
	set MonsterOldX = MonsterX
	return

' Espera la pulsación de una tecla
'
ReadKey:
	do
		set KeyPressed = key()
	loop while KeyPressed == ""
	return
