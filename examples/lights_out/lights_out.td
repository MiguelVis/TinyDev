' --------------------
' TinyDev: Lights Out!
' --------------------

' Images | Imágenes
'
variable image tile_dark           = "tile_dark.png"
variable image tile_light          = "tile_light.png"
variable image button_reload       = "btn_reload.png"
variable image button_reload_click = "btn_reload_click.png"
variable image button_stop         = "btn_stop.png"
variable image button_stop_click   = "btn_stop_click.png"

' Variables
'
variable number matrix[5 * 5]
variable number x
variable number y
variable string mouse_button
variable number mouse_x
variable number mouse_y
variable number game_over = 0
variable number button_pressed

' Setup screen | Inicializar pantalla
'
screen 16 + 5 * 96, 24 + 48 + 5 * 96
paper "orange"
clear

font "arial", 48, bold, transparent
pen "black"
cursor to 10, 2
print "Lights Out!"
pen "red"
cursor to 8, 0
print "Lights Out!"

set button_pressed = 0
call DrawReloadButton
call DrawStopButton

random 9
call SetupMatrix
call DrawMatrix

' Game loop | Bucle del juego
'
do
	' Wait a mouse click | Esperar un click del ratón
	do
		set mouse_button = mouseclick()
		set mouse_x = mousex()
		set mouse_y = mousey()
	loop while mouse_button != "left"

	' Click on a tile? | ¿Click en una casilla?
	if game_over == 0 && mouse_x > 8 && mouse_x < screenwidth() - 8 && mouse_y >= 64 && mouse_y < screenheight() - 8 then
		set x = (mouse_x - 8) / 96
		set y = (mouse_y - 64) / 96
		call ClickOnTile
		call DrawMatrix
		call CheckGameOver
	' Click on reload button? | ¿Click en el botón de recargar?
	else if mouse_x >= screenwidth() - 8 - 48 - 8 - 48 && mouse_x < screenwidth() - 8 - 48 - 8 && mouse_y >= 8 && mouse_y < 56 then
		set button_pressed = 1
		call DrawReloadButton
		wait 125
		set button_pressed = 0
		call DrawReloadButton
		call SetupMatrix
		call DrawMatrix
		set game_over = 0
	' Click on stop button? | ¿Click en el botón de terminar?
	else if mouse_x >= screenwidth() - 8 - 48 && mouse_x < screenwidth() - 8 && mouse_y >= 8 && mouse_y < 56 then
		set button_pressed = 1
		call DrawStopButton
		wait 125
		set button_pressed = 0
		call DrawStopButton
		font 64
		pen "red"
		if game_over == 0 then
			cursor to 0, 64 + 96 + 12
		else
			cursor to 0, 64 + 64 + 96 + 12
		end if
		print center "BYE!"
		break
	end if
loop while 1 == 1

' End of game | Fin del juego
'
end

' Setup matrix | Inicializar matriz
'
SetupMatrix:
	set x = 0
	do
		set y = 0
		do
			if random() < 5 then
				set matrix[y * 5 + x] = 0
			else
				set matrix[y * 5 + x] = 1
			end if
			set y = y + 1
		loop while y < 5
		set x = x + 1
	loop while x < 5
	return

' Draw matrix | Dibujar matriz
'
DrawMatrix:
	set x = 0
	do
		set y = 0
		do
			call DrawTile
			set y = y + 1
		loop while y < 5
		set x = x + 1
	loop while x < 5
	return

' Draw tile | Dibujar casilla
'
DrawTile:
	if matrix[y * 5 + x] == 0 then
		image 8 + x * 96, 64 + y * 96, tile_dark
	else
		image 8 + x * 96, 64 + y * 96, tile_light
	end if
	return

' Click on tile | Click en casilla
'
ClickOnTile:
	' Clicked tile | Casilla clickada
	set matrix[y * 5 + x] = 1 - matrix[y * 5 + x]

	' Up | Arriba
	if y > 0 then
		set matrix[(y - 1) * 5 + x] = 1 - matrix[(y - 1) * 5 + x]
	end if

	' Down | Abajo
	if y < 4 then
		set matrix[(y + 1) * 5 + x] = 1 - matrix[(y + 1) * 5 + x]
	end if

	' Left | Izquierda
	if x > 0 then
		set matrix[y * 5 + x - 1] = 1 - matrix[y * 5 + x - 1]
	end if

	' Right | Derecha
	if x < 4 then
		set matrix[y * 5 + x + 1] = 1 - matrix[y * 5 + x + 1]
	end if
	return

' Check game over | Comprobar si el juego ha terminado
'
CheckGameOver:
	set x = 0
	do
		set y = 0
		do
			if matrix[y * 5 + x] != 0 then
				return
			end if
			set y = y + 1
		loop while y < 5
		set x = x + 1
	loop while x < 5

	cursor to 0, 64 + 96 + 12
	font 64
	pen "red"
	print center "GAME OVER"

	set game_over = 1
	return

' Draw reload button | Dibujar botón de recargar
'
DrawReloadButton:
	if button_pressed == 0 then
		image screenwidth() - 8 - 48 - 8 - 48, 8, button_reload
	else
		image screenwidth() - 8 - 48 - 8 - 48, 8, button_reload_click
	end if
	return

' Draw stop button | Dibujar botón de terminar
'
DrawStopButton:
	if button_pressed == 0 then
		image screenwidth() - 8 - 48, 8, button_stop
	else
		image screenwidth() - 8 - 48, 8, button_stop_click
	end if
	return
