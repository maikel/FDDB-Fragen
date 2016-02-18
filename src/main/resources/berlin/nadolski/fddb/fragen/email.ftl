<!DOCTYPE html>
<html lang="de">
<head>
<title>Neue FDDB Nachrichten</title>
<meta charset="UTF-8">
</head>
<body>
<h1>Neue FDDB Nachrichten</h1>
<main>
   <#list questions as question>
      <article>
      <h2><a href="#title">${question.title}</a></h2>
      <author><a href="#author">${question.author}</a></author>
      <date>${question.date?date}, ${question.date?time}</date>
      <#list question.answers as answer>
         <p><a href="#author">${answer.author}</a></p>
         <p>${answer.text}</p>
      </#list>
      </article>
   </#list>
</main>
</body>
</html>