<%@ page import="java.util.List" %>
<%@ page import="models.Article" %>
<%
    List<Article> availableArticles = (List<Article>) request.getAttribute("availableArticles");
    double totalReturnedValue = (double) request.getAttribute("totalReturnedValue");
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Echanger des Articles</title>
    <script src="https://cdn.tailwindcss.com"></script>
 <script>
        function updateTotalReturnedValue() {
            const quantityInputs = document.querySelectorAll('input[name="articleQuantities"]');
            let totalCost = 0;

            quantityInputs.forEach(input => {
                const price = parseFloat(input.closest('tr').querySelector('[data-price]').getAttribute('data-price'));
                const quantity = parseInt(input.value) || 0;
                totalCost += price * quantity;
            });

            document.getElementById('total').textContent = totalCost.toFixed(2);
        }
    </script>
</head>
<body class="bg-gray-100 py-8">
    <div class="max-w-4xl mx-auto bg-white shadow-md rounded-lg p-6">
        <h2 class="text-2xl font-bold mb-4 text-gray-800">Echanger des Articles</h2>
        <div class="flex space-x-4">
        <p class="text-red-700 mb-6">
            Votre solde : 
            <span 
                id="totalValue" 
                class="font-semibold">
                <%= request.getAttribute("solde") %>
            </span>
        </p>
        <p class="text-gray-700 mb-6">
            Valeur totale des articles a echange :
            <span 
                id="total" 
                class="font-semibold">
                0.00
            </span>
        </p>
        <p class="text-gray-700 mb-6">
            Valeur totale retournee : 
            <span 
                id="totalValue" 
                class="font-semibold">
                <%= totalReturnedValue %>
            </span>
        </p>
        </div>
        <form 
            action="${pageContext.request.contextPath}/processExchange" 
            method="post" 
            class="space-y-6" 
            >
            <input type="hidden" name="retourId" value="<%= request.getParameter("retourId") %>">

            <div class="overflow-x-auto">
                <table class="table-auto w-full border-collapse border border-gray-200">
                    <thead>
                        <tr class="bg-gray-100">
                            <th class="border border-gray-300 px-4 py-2 text-left text-gray-800">Image de l'Article</th>
                            <th class="border border-gray-300 px-4 py-2 text-left text-gray-800">Nom de l'Article</th>
                            <th class="border border-gray-300 px-4 py-2 text-left text-gray-800">Prix</th>
                            <th class="border border-gray-300 px-4 py-2 text-left text-gray-800">Quantite</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Article article : availableArticles) { %>
                        <tr class="odd:bg-white even:bg-gray-50">
                            <td class="border border-gray-300 px-4 py-2 text-gray-700"><img src="<%= article.getImageUrl() %>" alt="<%= article.getNom() %>" class="w-[150px] h-[150px] object-cover rounded-lg mb-4"></td>
                            <td class="border border-gray-300 px-4 py-2 text-gray-700"><%= article.getNom() %></td>
                            <td class="border border-gray-300 px-4 py-2 text-gray-700" data-price="<%= article.getPrix() %>">
                                <%= article.getPrix() %>
                            </td>
                            <td class="border border-gray-300 px-4 py-2 text-gray-700">
                                <div class="flex items-center space-x-2">
                                    <input 
                                        type="number" 
                                        name="articleQuantities" 
                                        value="0" 
                                        min="0" 
                                        max="<%= article.getQuantite() %>" 
                                        class="w-20 px-2 py-1 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500" 
                                        onchange="updateTotalReturnedValue()">
                                    <input type="hidden" name="articleIds" value="<%= article.getId() %>">
                                </div>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>

            <div class="text-right">
                <button 
                    type="submit" 
                    class="bg-amber-500 text-white rounded-full px-6 py-2 shadow hover:bg-amber-600 focus:outline-none focus:ring-2 focus:ring-amber-400">
                    Confirmer l'echange
                </button>
            </div>
        </form>
    </div>
</body>
</html>
