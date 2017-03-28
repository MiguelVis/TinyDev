' -----------------------
' TinyDev: Juego de sumas
' -----------------------

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
variable number primerNumero   ' Primer número a sumar
variable number segundoNumero  ' Segundo número a sumar
variable number resultado      ' Resultado de la suma
variable number respuesta      ' Respuesta del jugador

' Inicialización
'
screen 800, 600
pen white
paper black

random 50  ' Obtendremos números del 0 al 50

' Presentación
'
clear
font 32
pen white
print break() + break()
print center "El juego de las"
print break() + break()
font 64
pen blue
print center "sumas"

' Pausa de 1,5 segundos
'
wait 1500

' Bucle principal
'
do
	call DameNumeros
	call PreguntaSuma
	call CompruebaSuma

	wait 1000

	dialog yesno "¿Quieres sumar más números?"
loop while reply() == "yes"

' Fin del juego
'
clear
font 64
pen white
print break() + break()
print center "¡Hasta luego!"
end

' Obtener números y el resultado
'
DameNumeros:
	set primerNumero = random()
	set segundoNumero = random()
	set resultado = primerNumero + segundoNumero
	return

' Preguntar la respuesta al jugador
'
PreguntaSuma:
	clear
	font 32
	pen white
	print center "¿Cuál es el resultado"
	print break()
	print center "de esta suma?"
	print break()
	call VerOperandos
	print center "?"
	print break()
	dialog input "Introduce el resultado:"
	set respuesta = number(reply())
	return

' Comprobar que el resultado coincide con lo que ha respondido
' el jugador
'
CompruebaSuma:
	clear
	font 32
	pen white
	print center "El resultado"
	print break()
	print center "de la suma es"
	print break()
	call verOperandos
	print center string(resultado)
	print break()
	font 64
	pen red
	if respuesta == resultado then print center "¡Muy bien!"
	else print center "¡No son " + string(respuesta) + "!"
	return

' Imprime los operandos de la suma,
' sin el resultado
'
VerOperandos:
	font 64
	pen blue
	print center string(primerNumero)
	print break()
	font 32
	print center "+"
	print break()
	font 64
	print center string(segundoNumero)
	print break()
	font 32
	print center "----------"
	print break()
	font 64
	pen red
	return
