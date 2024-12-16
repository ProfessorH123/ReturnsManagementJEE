<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="models.Commande, models.Article" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historique des Commandes</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
   	<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>

<body class="bg-gray-100 font-sans">
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
        <% if (request.getAttribute("message") != null) { %>
		    <script>
		        alert("<%= request.getAttribute("message") %>");
		    </script>
		<% } %>

    <div class="container mx-auto px-4 py-8">
		<h1 class="text-4xl font-extrabold text-gray-800 mb-6 leading-tight text-center border-b-4 border-amber-500 pb-2">
		    Historique des commandes
		</h1>
		<div class="text-center my-4">
		    <p class="text-xl text-amber-600 font-semibold">
		        Solde actuel : <span class="font-bold"><%= String.format("%.2f", request.getAttribute("solde")) %> €</span>
		    </p>
		</div>
        <% 
        List<Commande> commandes = (List<Commande>) request.getAttribute("commandes");
        if (commandes == null || commandes.isEmpty()) { %>
            <p class="text-gray-600">Aucune commande trouvée pour cet utilisateur.</p>
        <% } else { %>
<form action="${pageContext.request.contextPath}/retour" method="get">
    <button type="submit" class="bg-amber-500 text-white float-right mb-6 py-2 px-4 rounded-full hover:bg-amber-700 transition duration-300 ease-in-out transform hover:scale-105">
        Voir les Retours
    </button>
</form>


            <table class="min-w-full table-auto text-sm bg-white">
                <thead class="bg-gray-200 text-gray-700">
                    <tr>
                        <th class="px-6 py-4 text-left font-medium">#</th>
                        <th class="px-6 py-4 text-left font-medium">Date</th>
                        <th class="px-6 py-4 text-left font-medium">Articles et Quantités</th>
                        <th class="px-6 py-4 text-left font-medium">Prix Total</th>
                        <th class="px-6 py-4 text-left font-medium">Statut</th>
                        <th class="px-6 py-4 text-left font-medium">Actions</th>
                    </tr>
                </thead>
<tbody class="text-gray-600">
    <% int nbC = 1; for (Commande commande : commandes) { double total = 0; boolean isCancelled = "Annulé".equals(commande.getStatus()); %>
        <tr class="border-t <%= isCancelled ? "bg-red-200 text-white" : "" %>">
            <td class="px-6 py-4"><%= nbC++ %></td>
            <td class="px-6 py-4"><%= commande.getDateCommande() %></td>
            <td class="px-6 py-4">
                <ul class="list-disc pl-6 space-y-2">
                    <% for (Article article : commande.getArticles()) {
                        total += article.getPrix() * article.getQuantite(); %>
                        <li><%= article.getNom() %> - <%= article.getQuantite() %> unité(s) - <%= article.getPrix() %> €/unité</li>
                    <% } %>
                </ul>
            </td>
            <td class="px-6 py-4">
                <%= String.format("%.2f", total) %> €
            </td>
            <td class="px-6 py-4">
                <%= commande.getStatus() %>
            </td>
            <td class="px-6 py-4">

				 <% if (!isCancelled) { %>
                    <form action="<%= request.getContextPath() %>/addRetour" method="get">
                        <input type="hidden" name="idCommande" value="<%= commande.getId() %>" />
                        <button type="submit" class="bg-green-600 text-white py-1 px-4 rounded-lg hover:bg-green-700 transition duration-300">
                            Retourner article(s)
                        </button>
                    </form>
                <% } %>
            </td>
        </tr>
    <% } %>
</tbody>

            </table>        <% } %>
    </div>
</body>

</html>
