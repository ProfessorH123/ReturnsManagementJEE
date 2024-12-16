<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Créer un compte</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 flex items-center justify-center min-h-screen font-sans">

    <div class="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
        <h1 class="text-2xl font-semibold text-center text-gray-800 mb-6">Créer un compte</h1>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null && !error.isEmpty()) {
        %>
            <p class="text-red-500 text-center mb-4"><%= error %></p>
        <%
            }
        %>

        <%
            String message = (String) request.getAttribute("message");
            if (message != null && !message.isEmpty()) {
        %>
            <p class="text-green-500 text-center mb-4"><%= message %></p>
        <%
            }
        %>

        <form action="<%= request.getContextPath() %>/creerCompte" method="POST">
            <input type="hidden" name="action" value="registre" />

            <div class="mb-4">
                <label for="email" class="block text-sm font-medium text-gray-700">Email :</label>
                <input type="email" id="email" name="email" placeholder="Votre email" required
                    class="mt-1 block w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"/>
            </div>

            <div class="mb-4">
                <label for="password" class="block text-sm font-medium text-gray-700">Mot de passe :</label>
                <input type="password" id="password" name="password" placeholder="Votre mot de passe" required
                    class="mt-1 block w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"/>
            </div>

            <div class="mb-6">
                <label for="confirmPassword" class="block text-sm font-medium text-gray-700">Confirmer le mot de passe :</label>
                <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirmez votre mot de passe" required
                    class="mt-1 block w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"/>
            </div>

            <button type="submit" class="w-full bg-amber-600 text-white py-2 px-4 rounded-md hover:bg-amber-700 transition duration-300">Créer un compte</button>
        </form>

        <p class="mt-4 text-center text-sm text-gray-600">
            Déjà un compte ? <a href="login.jsp" class="text-amber-600 hover:underline">Connectez-vous ici</a>
        </p>
    </div>
</body>
</html>
