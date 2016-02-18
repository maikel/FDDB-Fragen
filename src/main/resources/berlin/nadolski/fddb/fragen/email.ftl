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
      <p class="text">${question.text}</p>
      <#list question.answers as answer>
         <author><a href="#author">${answer.author}</a></author>
         <p class="text">${answer.text}</p>
      </#list>
      </article>
   </#list>
</main>
</body>
</html>