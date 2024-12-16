<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="models.Commande, models.Article, models.Retour" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>All Retours</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100">
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
    <div class="container mx-auto  py-8 px-4">
    	<h1 class="text-4xl font-extrabold text-gray-800 mb-6 leading-tight text-center border-b-4 border-amber-500 pb-2">
			Liste des Retours
		</h1>
        <% if (request.getAttribute("message") != null) { %>
		    <script>
		        alert("<%= request.getAttribute("message") %>");
		    </script>
		<% } %>
        
        <div class="overflow-x-auto bg-white shadow-md rounded-lg">
            <table class="min-w-full table-auto">
                <thead>
                    <tr class="bg-gray-200 text-left">
                        <th class="px-4 py-2">Retour ID</th>
                        <th class="px-4 py-2">Commande ID</th>
                        <th class="px-4 py-2">Statut</th>
                        <th class="px-4 py-2">Raison</th>
                        <th class="px-4 py-2">Email de utilisateur</th>
                        <th class="px-4 py-2">Date de retour</th>
                        <th class="px-4 py-2">Articles</th>
                        <th class="px-4 py-2">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                    List<Retour> retours = (List<Retour>) request.getAttribute("retours");
                    if (retours != null) {
                        for (Retour retour : retours) {
                    %>
                        <tr class="border-b">
                            <td class="px-4 py-2"><%= retour.getId() %></td>
                            <td class="px-4 py-2"><%= retour.getIdCommande() %></td>
                            <td class="px-4 py-2"><%= retour.getStatus().equals("Accepted") ? "Accepter" : "En cours" %></td>
                            <td class="px-4 py-2"><%= retour.getRaison() %></td>
                            <td class="px-4 py-2"><%= retour.getUserMail() %></td>
                            <td class="px-4 py-2"><%= retour.getRetourDate() %></td>
                            <td class="px-4 py-2">
                                <% 
                                List<Article> articles = retour.getArticles();
                                if (articles != null) {
                                    for (Article article : articles) {
                                        out.print(article.getNom()+" - "+ article.getQuantite() + " unité(s)" + "<br>");
                                    }
                                }
                                %>
                            </td>
							<td class="px-4 py-2">
							    <% if (!"Accepted".equals(retour.getStatus())) { %>
							        <form action="/project_client/acceptRetour" method="POST">
							            <input type="hidden" name="retourId" value="<%= retour.getId() %>">
							            <button type="submit" class="bg-amber-500 rounded-full transition duration-300 ease-in-out transform hover:scale-105 text-white px-4 py-2 rounded">Accepter le retour</button>
							        </form>
							    <% } else { %>
							        <span class="text-gray-500">Retour déja accepter</span>
							    <% } %>
							</td>


                        </tr>
                    <% 
                        }
                    }
                    %>
                </tbody>
            </table>
        </div>
    </div>

</body>
</html>
