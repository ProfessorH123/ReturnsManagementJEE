<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="models.Article, models.Commande" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Retourner des Articles</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-gray-50">
	<nav class="bg-white shadow-lg">
	    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
	        <div class="flex justify-between items-center h-16">
	            <div class="flex-shrink-0">
	                <a class="text-2xl font-bold text-amber-500">JEE PROJECT</a>
	            </div>
	
        <div class="mb-4">
            <form action="${pageContext.request.contextPath}/deconnecter" method="post">
                <button type="submit" class="bg-red-500 text-white py-2 px-4 rounded hover:bg-red-700">
                    <i class="fas fa fa-arrow-right-from-bracket"></i>
                </button>
            </form>
        </div>
	
	        </div>
	    </div>
	
	</nav>
    <div class="container mx-auto px-4 py-8">
		<h1 class="text-4xl font-extrabold text-gray-800 mb-6 leading-tight text-center border-b-4 border-amber-500 pb-2">
		    Retourner des articles
		</h1>
        <%
            Commande commande = (Commande) request.getAttribute("commande");
            if (commande != null) {
        %>

            <form id="returnForm" action="${pageContext.request.contextPath}/addRetour" method="post" class="bg-white p-6 rounded-lg shadow-md">
                <input type="hidden" name="idCommande" value="<%= commande.getId() %>" />

                <h3 class="text-xl font-semibold text-gray-800 mb-4">Articles de la commande <%= commande.getId() %> :</h3>
                
                <ul class="space-y-4">
                <%
                    // Display articles and quantities
                    for (Article article : commande.getArticles()) {
                %>
                    <li class="flex justify-between items-center p-4 border border-gray-200 rounded-md">
                        <div class="flex flex-col">
                            <span class="font-medium text-gray-700">Article: <%= article.getNom() %></span>
                            <span class="text-sm text-gray-500">Quantité disponible: <%= article.getQuantite() %></span>
                        </div>
                        <div class="flex flex-col">
                            <label for="returnQuantity_<%= article.getId() %>" class="text-sm text-gray-700">Quantité à retourner:</label>
                            <input 
                                type="number" 
                                id="returnQuantity_<%= article.getId() %>" 
                                name="returnQuantity_<%= article.getId() %>" 
                                min="0" 
                                max="<%= article.getQuantite() %>" 
                                value="0" 
                                required 
                                class="mt-1 p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 returnQuantity"
                                onchange="validateForm()"
                            >
                        </div>
                    </li>
                <%
                    }
                %>
                </ul>

                <div class="mt-6">
                    <label for="raison" class="block text-sm font-medium text-gray-700">Raison du retour:</label>
                    <textarea 
                        id="raison" 
                        name="raison" 
                        required 
                        class="mt-2 p-3 border border-gray-300 rounded-md w-full focus:outline-none focus:ring-2 focus:ring-blue-500" 
                        oninput="validateForm()"
                    ></textarea>
                </div>

                <button 
                    id="submitButton" 
                    type="submit" 
                    class="mt-6 w-full bg-amber-600 text-white py-3 px-4 rounded-md hover:bg-amber-700 focus:outline-none focus:ring-2 focus:ring-amber-500 disabled:opacity-50 disabled:cursor-not-allowed" 
                    disabled
                >
                    Confirmer le retour
                </button>
            </form>

        <% 
            } else {
                out.print("<p class='text-red-500 text-lg'>Commande non trouvée.</p>");
            }
        %>
    </div>

    <script>
        function validateForm() {
            const quantities = document.querySelectorAll('.returnQuantity');
            const reason = document.getElementById('raison').value.trim();
            const submitButton = document.getElementById('submitButton');

            // Check if at least one quantity > 0
            let hasQuantity = false;
            quantities.forEach(input => {
                if (parseInt(input.value) > 0) {
                    hasQuantity = true;
                }
            });

            // Enable or disable the button
            if (hasQuantity && reason !== '') {
                submitButton.disabled = false;
            } else {
                submitButton.disabled = true;
            }
        }
    </script>

</body>
</html>
