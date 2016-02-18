<!DOCTYPE html>
<html lang="de">
<head>
<title>Neue FDDB Nachrichten</title>
<meta charset="UTF-8">
<style>
body {
   font-size: 11pt;
}
h2 {
   font-size: 14pt;
}
.blue, a {
   color: #24a;
}
author {
   display: block;
}
article {
   width: 78ch;
}
article article {
   border-top: 1px solid #eee;
   margin-top: 1ex;
   padding-top: 1ex;
}
p {
   margin: 0;
   padding: 1ch;
}
</style>
</head>
<body>
<h1>Neue FDDB Nachrichten</h1>
<main>
   <#list questions as question>
      <article>
      <h2>Titel: "<a href=\"${question.href}\">${question.title}</a>"</h2>
      <author>Autor: <span class="blue">${question.author}</span></author>
      <date>Veröffentlicht am ${question.date?date} um ${question.date?time}</date>
      <p class=\"text\">Frage: ${question.text}</p>
      <#list question.answers as answer>
         <article>
         <author>${answer.author}</author>
         <p class=\"text\">${answer.text}</p>
         </article>
      </#list>
      </article>
   </#list>
</main>
</body>
</html>