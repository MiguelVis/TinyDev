// Globals
var td_language = "en";
var td_tx;

/**
 *  Devuelve el valor de un parámetro, leyéndolo de los
 *  argumentos de la URL.
 *
 *  @return   Valor del parámetro, o el valor por defecto,
 *            si no existe, o parece incorrecto.
 */
function getValor(parametro, defecto) {

	// Tomar URL
	var url = decodeURI(new String(document.location.href));

	// Comprobar si es el primer argumento
	var posArgs = url.indexOf("?" + parametro + "=");
	
	// Si no es el 1º, comprobar si es alguno posterior
	if(posArgs == -1) {
		posArgs = url.indexOf("&" + parametro + "=");
	}
	
	// Tratar el argumento si se ha encontrado
	if(posArgs != -1) {
		
		// Tomar cadena a partir de la posición encontrada
		var arg = url.substring(posArgs, url.length);
		
		// Tomar cadena a partir del signo igual
		arg = arg.substring(arg.indexOf("=") + 1, arg.length);
		
		// Calcular final de la cadena
		
		// Puede seguirle otro argumento
		var fin = arg.indexOf("&");
		
		// O puede ser el último
		if(fin == -1) {
			fin = arg.length;
		}
		
		// Tomar el valor del argumento
		arg = decodeURIComponent(arg.substring(0, fin));
		
		// Devolver el valor si parece correcto
		if(arg.length > 0) {
			return arg;
		}
	}
	
	// No existe el argumento, o no parece válido;
	// devolver el valor por defecto.
	return defecto;
}


function td_setup()
{
	var lang = getValor("lang", navigator.language);
	
	if(lang == undefined)
	{
		// IE <= 10
		lang = navigator.browserLanguage;
		
		if(lang == undefined)
		{
			lang = "en";
		}
	}

	if(lang.indexOf("es") != -1) {
		td_language = "es";
		
		td_tx = {
			title_programming_for_kids: "Programación para niños",
			title_overview: "Introducción",
			title_news: "Novedades",
			title_the_ide: "El IDE",
			title_examples: "Ejemplos",
			title_requirements: "Requisitos",
			title_contact: "Contacto",
			title_downloads: "Descargas",
			title_snippets: "Snippets",
			
			ide_intro: "El IDE (Entorno de Desarrollo Integrado) de TinyDev ha sido diseñado para parecerse y actuar como los que acompañan a los lenguajes de programación profesionales. Admite los idiomas inglés y español.",
			ide_1: "Tiene las funciones características de este tipo de entornos, entre ellas:",
			ide_2: "Crear, abrir, guardar e imprimir ficheros de código fuente.",
			ide_3: "Editar el código fuente de los programas.",
			ide_4: "Ejecutar y parar el programa actual.",
			ide_5: "Configurar el IDE.",
			ide_6: "Mostrar ayuda sobre el IDE y el lenguaje de programación.",
			ide_7: "Botones de acceso rápido a las acciones más frecuentes.",
			ide_8: "Panel de selección rápida con todas las palabras clave del lenguaje.",
			ide_9: "Panel de información sobre el código fuente actual.",
			
			over_1: "TinyDev es un lenguaje de programación para niños, creado con el objetivo de que aprendan a resolver problemas.",
			over_2: "Aunque es un lenguaje muy completo, ha sido diseñado para que sea sencillo de utilizar, huyendo de las complicaciones y dificultades de ciertos lenguajes de programación para niños, muy en boga actualmente.",
			over_3: "Mediante TinyDev, los niños pueden dibujar gráficos, escribir texto, mover imágenes en la pantalla, emitir sonidos, e interactuar con sus propios programas mediante el teclado y el ratón.",
			over_4: "Además, se acompaña de un pequeño IDE (Integrated Development Environment, Entorno de Desarrollo Integrado), mediante el cual los niños pueden crear, editar y ejecutar sus programas, tal y como ocurre con los lenguajes de programación profesionales.",
			over_5: "Y... ¡es gratuito!",
			
			req_intro: "Los requisitos para instalar y ejecutar TinyDev, son los siguientes:",
			req_1: "Ordenador con Java 1.8 o superior.",
			req_2: "Ganas de aprender y pasarlo bien.",
			
			ex_intro: "Con el objetivo de ayudar a los más pequeños en el aprendizaje del lenguaje de programación, han sido desarrollados algunos programas de ejemplo, que muestran las posibilidades de TinyDev.<br><br>Entre ellos, están:",
			ex_1: "<strong>Chase</strong> - un juego en el que dos robots pretenden atraparte.",
			ex_2: "<strong>Space</strong> - el clásico juego de naves espaciales y asteroides.",
			ex_3: "<strong>Memory</strong> - un juego de fichas de colores, para ejercitar nuestra memoria.",
			ex_4: "Simulación de una <strong>pecera</strong>.",
			ex_5: "El clásico juego del <strong>tres en raya</strong>.",
			ex_6: "<strong>Dibujos</strong> - camión, edificio, efectos de pantalla...",
			ex_7: "<strong>Lights Out!</strong> - ¡apaga las luces!",
			
			down_intro: "Puedes descargar y utilizar TinyDev gratuitamente, pero está prohibido copiarlo, modificarlo, licenciarlo, venderlo o distribuirlo, sin el permiso escrito del autor. Para más información, consulta el texto completo de la licencia que acompaña a TinyDev.",
			down_github: "En nuestro <a href='https://github.com/MiguelVis/TinyDev' target='_blank'>repositorio de GitHub</a>, siempre encontrarás las últimas actualizaciones y nuevos ejemplos.",
			
			news_1_title: "TinyDev v1.00",
			news_1_date: "18/03/2017",
			news_1_text: "La primera versión de TinyDev, ya está disponible para descarga gratuita.<br><br>¡A divertirse programando!",
			
			news_2_title: "Página web",
			news_2_date: "21/03/2017",
			news_2_text: "Estrenamos página web para TinyDev, <i>responsive</i> y con nuevos contenidos.<br><br>¡Esperamos que os guste!",
			
			news_3_title: "Snippets",
			news_3_date: "23/03/2017",
			news_3_text: "Tenemos una nueva sección llamada <i>snippets</i>. En ella, publicaremos algunos simpáticos fragmentos de código.<br><br>¡Disfrútalos!",
			
			news_4_title: "Lights Out!",
			news_4_date: "31/12/2017",
			news_4_text: "Antes de que finalice este año, os ofrecemos un nuevo juego, con el que disfutar y aprender a programar: <strong><a href='https://github.com/MiguelVis/TinyDev/tree/master/examples/lights_out' target='_blank'>Lights Out!</a></strong><br><br>¡Apaga todas las luces para ganar!",
			
			snip_1_title: "Imprimir texto con sombra",
			
			snip_code: "Código:",
			snip_result: "Resultado:",
			
			all_rights_reserved: "Todos los derechos reservados.",
			
			close_menu: "Cerrar menú",
			
			eof: "eof"		
		};
	}
	else {
		td_language = "en";
		
		td_tx = {
			title_programming_for_kids: "Programming for kids",
			title_overview: "Overview",
			title_news: "News",
			title_the_ide: "The IDE",
			title_examples: "Examples",
			title_requirements: "Requirements",
			title_contact: "Contact",
			title_downloads: "Downloads",
			title_snippets: "Snippets",
			
			ide_intro: "The IDE (Integrated Development Environment) of TinyDev has been designed to seem and act like the ones that come with the professional programming languages. Supports the English and Spanish languages.",
			ide_1: "It has the characteristic functions of this kind of environments, among them:",
			ide_2: "Create, open, save and print source code files.",
			ide_3: "Edit source code of programs.",
			ide_4: "Run and stop the current program.",
			ide_5: "Configure the IDE.",
			ide_6: "Show help about the IDE and the programming language.",
			ide_7: "Fast access buttons to the most frequent actions.",
			ide_8: "Fast selection panel with all the keywords of the language.",
			ide_9: "Information panel about the current source code.",
			
			over_1: "TinyDev is a programming language for kids. It was created with the goal they learn how to resolve logical problems.",
			over_2: "Although it's a very complete language, it has been designed to be easy using it, avoiding the complications and difficulties of certain programming languages for kids, very much in vogue today.",
			over_3: "Using TinyDev, the kids can draw graphics, write text, move images on screen, play sounds, and interact with their programs with the keyboard and the mouse.",
			over_4: "In addition, it's accompanied by a small IDE (Integrated Development Environment). With it, the kids can create, edit and run their own programs, as with professional programming languages.",
			over_5: "And... it's free!",
			
			req_intro: "The requirements to install and run TinyDev are the following:",
			req_1: "Computer with Java 1.8 or higher.",
			req_2: "Want to learn and have fun.",
			
			ex_intro: "With the aim to help our little kids to learn this programming language, it has been developed some example programs, which show the capabilities of TinyDev.<br><br>Some of them are:",
			ex_1: "<strong>Chase</strong> - a game in which two robots want to catch you.",
			ex_2: "<strong>Space</strong> - the classic game of space ships and asteroids.",
			ex_3: "<strong>Memory</strong> - a game with coloured tokens, to exercise our memory.",
			ex_4: "Simulation of an <strong>aquarium</strong>.",
			ex_5: "The classic game of <strong>tic-tac-toe</strong>.",
			ex_6: "<strong>Drawings</strong> - truck, building, screen effects...",
			ex_7: "<strong>Lights Out!</strong> - ¡turn off the lights!",
			
			down_intro: "You can download and use TinyDev for free, but copying, modifying, licensing, shelling or distributing it is forbidden, without the written permission of the author. For more information, read the full text of the license that accompanies to TinyDev.",
			down_github: "In our <a href='https://github.com/MiguelVis/TinyDev' target='_blank'>GitHub repository</a>, you will always find the last updates and new examples.",
			
			news_1_title: "TinyDev v1.00",
			news_1_date: "18 Mar 2017",
			news_1_text: "The first version of TinyDev is out for free download.<br><br>Enjoy doing some programming!",
			
			news_2_title: "Web page",
			news_2_date: "21 Mar 2017",
			news_2_text: "We have a new page for TinyDev, responsive and with new contents.<br><br>We hope you like it!",
			
			news_3_title: "Snippets",
			news_3_date: "23 Mar 2017",
			news_3_text: "We have a new section called <i>snippets</i>. We will publish some nice fragments of code there.<br><br>Enjoy!",
			
			news_4_title: "Lights Out!",
			news_4_date: "31 Dec 2017",
			news_4_text: "Before the end of this year, we offer you a new game, to enjoy and learn computer programming: <strong><a href='https://github.com/MiguelVis/TinyDev/tree/master/examples/lights_out' target='_blank'>Lights Out!</a></strong><br><br>Switch off all the lights to win!",
			
			snip_1_title: "Print text with shadow",
			
			snip_code: "Code:",
			snip_result: "Result:",
			
			all_rights_reserved: "All rights reserved.",
			
			close_menu: "Close menu",
			
			eof: "eof"		
		};
	}
}


function td_translate(key)
{
	document.write(td_tx[key]);
}


function td_select(obj)
{
	var defaultClasses = "w3-bar-item w3-button w3-padding";
	
	document.getElementById("menu_news").className = defaultClasses;
	document.getElementById("menu_overview").className = defaultClasses;
	document.getElementById("menu_ide").className = defaultClasses;
	document.getElementById("menu_examples").className = defaultClasses;
	document.getElementById("menu_snippets").className = defaultClasses;
	document.getElementById("menu_requirements").className = defaultClasses;
	document.getElementById("menu_downloads").className = defaultClasses;
	document.getElementById("menu_contact").className = defaultClasses;
	
	obj.className = defaultClasses + " w3-blue";
	
	//window.location = "#" + obj.id;
}

function td_toggle_snippet(obj_code, obj_btn)
{
	var show = document.getElementById(obj_code).style.display == "none";
	
	document.getElementById(obj_code).style.display = (show ? "block" : "none");
	obj_btn.className = (show ? "fa fa-chevron-up fa-fw" : "fa fa-chevron-down fa-fw");
}
