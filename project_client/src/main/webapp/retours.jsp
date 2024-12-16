<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="models.Commande, models.Article, models.Retour" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Retours</title>
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
			Liste des Retours
		</h1>

        <%
            List<Retour> retours = (List<Retour>) request.getAttribute("retours");
            if (retours != null && !retours.isEmpty()) {
        %>

            <table class="min-w-full bg-white border border-gray-200 rounded-lg shadow-md">
                <thead>
                    <tr class="bg-gray-100 text-left text-gray-600">
                        <th class="px-4 py-2 border-b">Retour ID</th>
                        <th class="px-4 py-2 border-b">Commande ID</th>
                        <th class="px-4 py-2 border-b">Articles Retournés</th>
                        <th class="px-4 py-2 border-b">Raison</th>
                        <th class="px-4 py-2 border-b">Date</th>
                        <th class="px-4 py-2 border-b">Options</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (Retour retour : retours) {
                            boolean isAccepted = "Accepted".equalsIgnoreCase(retour.getStatus());
                            boolean isReturned = "yes".equalsIgnoreCase(retour.getReturned());
                    %>
						<tr class="hover:cursor-pointer <%= retour.getReturned().equals("yes") ? "bg-green-200" : "" %>">
                            <td class="px-4 py-2 border-b"><%= retour.getId() %></td>
                            <td class="px-4 py-2 border-b"><%= retour.getIdCommande() %></td>
                            <td class="px-4 py-2 border-b">
                                <ul class="list-disc pl-5 space-y-1">
                                    <%
                                        for (Article article : retour.getArticles()) {
                                    %>
                                        <li><%= article.getNom() %> - Quantité: <%= article.getQuantite() %> prix total: <%= article.getQuantite() * article.getPrix() %></li>
                                    <%
                                        }
                                    %>
                                </ul>
                            </td>
                            <td class="px-4 py-2 border-b"><%= retour.getRaison() %></td>
                            <td class="px-4 py-2 border-b"><%= retour.getRetourDate() %></td>
                           <td class="px-4 py-2 border-b">
							    <% if (isAccepted && !isReturned) { %>
							        <div class="flex space-x-4">
							       <form id="refundForm-<%= retour.getId() %>" action="<%= request.getContextPath() %>/processRetour" method="POST" onsubmit="return confirmRefund(<%= retour.getId() %>)">
								    <input type="hidden" name="retourId" value="<%= retour.getId() %>">
								    <input type="hidden" id="totalPrice-<%= retour.getId() %>" name="totalPrice" value="<%= retour.getArticles().stream().mapToDouble(article -> article.getPrix() * article.getQuantite()).sum() %>">
								    <div class="flex space-x-4 items-center">
								        <button title="Remboursement" type="submit" name="action" value="remboursement" class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 flex items-center">
								            <i class="fas fa-sync-alt mr-2"></i> <!-- Font Awesome icon for Refund -->
								        </button>
								    </div>
								</form>
								
								<form id="refundForm-<%= retour.getId() %>" action="<%= request.getContextPath() %>/exchange" method="POST" onsubmit="return confirmRefund(<%= retour.getId() %>)">
								    <input type="hidden" name="retourId" value="<%= retour.getId() %>">
								    <div class="flex space-x-4 items-center">
								        <button title="Echange" type="submit" name="action" value="echange" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 flex items-center">
								            <i class="fas fa-exchange-alt mr-2"></i> <!-- Font Awesome icon for Exchange -->
								        </button>
								    </div>
								</form>
								</div>
							    <% } else { %>
							        <p class="text-gray-500 text-sm">Aucune option disponible.</p>
							    <% } %>
							</td>

                        </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>

        <%
            } else {
        %>
            <p class="text-gray-500 text-lg">Aucun retour trouvé.</p>
        <%
            }
        %>
    </div>
<script>
    function confirmRefund(retourId) {
        // Retrieve the total price
        const totalPrice = document.getElementById(`totalPrice-${retourId}`).value;

        // Display a confirmation alert
        const userConfirmed = confirm(`Le montant total des articles retournés est de ${totalPrice} €. Confirmez-vous le remboursement ?`);
        
        // Return user's choice
        return userConfirmed;
    }
</script>

</body>
</html>
