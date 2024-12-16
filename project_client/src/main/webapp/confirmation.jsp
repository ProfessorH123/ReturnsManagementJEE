<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirmation de l'Échange</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 py-8">

    <div class="max-w-lg mx-auto bg-white shadow-lg rounded-lg p-8">
        <h1 class="text-3xl font-semibold text-gray-800 mb-6">Confirmation de l'Échange</h1>
        <% if (request.getAttribute("message") != null) { %>
        <div id="message" class="mb-6 text-center text-green py-3 px-4 rounded-lg">
            <p id="message-text"><%= request.getAttribute("message") %></p>
        </div>
        
		<% } %>
        

        <form action="<%= request.getContextPath() %>/historique" method="get" class="text-center">
            <input type="hidden" name="email" value="<%= (String) request.getSession().getAttribute("userMail") %>" />
            <div>
                <button type="submit" class="bg-amber-500 text-white px-6 py-3 rounded-lg shadow-md hover:bg-amber-600 focus:outline-none focus:ring-2 focus:ring-amber-400 transition duration-300">
                    Retour à l'Historique des Commandes
                </button>
            </div>
        </form>
    </div>

</body>
</html>
