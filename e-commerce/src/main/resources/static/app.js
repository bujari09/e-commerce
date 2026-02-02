const API_BASE = 'http://localhost:8081/api';

// Auth state
let isLoggedIn = false;

// Show/hide sections
function showSection(sectionId) {
    document.getElementById('customerSection').style.display = 'none';
    document.getElementById('adminSection').style.display = 'none';
    if (sectionId !== 'customerSection' && sectionId !== 'adminSection') {
        return;
    }
    document.getElementById(sectionId).style.display = 'block';
}

// Utility functions
function showMessage(elementId, message, type = 'success') {
    const element = document.getElementById(elementId);
    if (!element) return;
    
    element.innerHTML = `<div class="alert alert-${type} alert-dismissible fade show" role="alert">
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>`;
    setTimeout(() => {
        element.innerHTML = '';
    }, 5000);
}

function clearForm(formId) {
    document.getElementById(formId).reset();
}

// Auth functions
async function performLogin() {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    
    try {
        const response = await apiCall('/auth/login', 'POST', { username, password });
        
        if (response.success) {
            isLoggedIn = true;
            localStorage.setItem('isLoggedIn', 'true');
            localStorage.setItem('authToken', response.token);
            showMessage('loginMessage', 'Login i suksesshëm!', 'success');
            
            // Close modal and show admin section
            const modal = bootstrap.Modal.getInstance(document.getElementById('loginModal'));
            modal.hide();
            
            setTimeout(() => {
                showAdminView();
            }, 500);
        } else {
            showMessage('loginMessage', response.message || 'Login i gabuar!', 'danger');
        }
    } catch (error) {
        showMessage('loginMessage', 'Gabim: ' + error.message, 'danger');
    }
}

async function logout() {
    try {
        await apiCall('/auth/logout', 'POST');
    } catch (error) {
        console.log('Logout error:', error);
    }
    
    isLoggedIn = false;
    localStorage.removeItem('isLoggedIn');
    localStorage.removeItem('authToken');
    showCustomerView();
}

function checkAuth() {
    isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';
    if (isLoggedIn) {
        showAdminView();
    } else {
        showCustomerView();
    }
}

function showAdminView() {
    showSection('adminSection');
    loadCities();
    loadSuppliers();
    loadCategories();
    loadProducts();
    loadCategoriesForProduct();
    loadSuppliersForProduct();
}

function showCustomerView() {
    showSection('customerSection');
    loadCustomerProducts();
}

// API functions
async function apiCall(endpoint, method = 'GET', data = null) {
    const options = {
        method,
        headers: {
            'Content-Type': 'application/json',
        }
    };
    
    if (data && method !== 'GET') {
        options.body = JSON.stringify(data);
    }
    
    try {
        const response = await fetch(`${API_BASE}${endpoint}`, options);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('API call failed:', error);
        throw error;
    }
}

// Cities functions
document.getElementById('cityForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const cityName = document.getElementById('cityName').value;
    
    try {
        await apiCall('/cities', 'POST', { name: cityName });
        showMessage('cityMessage', 'Qyteti u shtua me sukses!', 'success');
        clearForm('cityForm');
        loadCities();
    } catch (error) {
        showMessage('cityMessage', 'Gabim: ' + error.message, 'danger');
    }
});

async function loadCities() {
    try {
        const cities = await apiCall('/cities');
        const citiesList = document.getElementById('citiesList');
        citiesList.innerHTML = cities.map(city => 
            `<div class="list-group-item">
                <strong>${city.name}</strong> <small class="text-muted">(ID: ${city.id})</small>
            </div>`
        ).join('');
    } catch (error) {
        console.error('Failed to load cities:', error);
    }
}

// Suppliers functions
document.getElementById('supplierForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const supplierData = {
        name: document.getElementById('supplierName').value,
        email: document.getElementById('supplierEmail').value,
        phone: document.getElementById('supplierPhone').value
    };
    
    try {
        await apiCall('/suppliers', 'POST', supplierData);
        showMessage('supplierMessage', 'Furnizuesi u shtua me sukses!', 'success');
        clearForm('supplierForm');
        loadSuppliers();
        loadSuppliersForProduct();
    } catch (error) {
        showMessage('supplierMessage', 'Gabim: ' + error.message, 'danger');
    }
});

async function loadSuppliers() {
    try {
        const suppliers = await apiCall('/suppliers');
        const suppliersList = document.getElementById('suppliersList');
        suppliersList.innerHTML = suppliers.map(supplier => 
            `<div class="list-group-item">
                <strong>${supplier.name}</strong>
                ${supplier.email ? `<br><small class="text-muted">${supplier.email}</small>` : ''}
                <small class="text-muted">(ID: ${supplier.id})</small>
            </div>`
        ).join('');
    } catch (error) {
        console.error('Failed to load suppliers:', error);
    }
}

async function loadSuppliersForProduct() {
    try {
        const suppliers = await apiCall('/suppliers');
        const productSupplier = document.getElementById('productSupplier');
        productSupplier.innerHTML = '<option value="">Zgjidh furnizuesin</option>' +
            suppliers.map(supplier => 
                `<option value="${supplier.id}">${supplier.name}</option>`
            ).join('');
    } catch (error) {
        console.error('Failed to load suppliers for product:', error);
    }
}

// Categories functions
document.getElementById('categoryForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const categoryName = document.getElementById('categoryName').value;
    const categoryDescription = document.getElementById('categoryDescription').value;
    
    try {
        await apiCall('/categories', 'POST', { 
            name: categoryName, 
            description: categoryDescription 
        });
        showMessage('categoryMessage', 'Kategoria u shtua me sukses!', 'success');
        clearForm('categoryForm');
        loadCategories();
        loadCategoriesForProduct();
    } catch (error) {
        showMessage('categoryMessage', 'Gabim: ' + error.message, 'danger');
    }
});

async function loadCategories() {
    try {
        const categories = await apiCall('/categories');
        const categoriesList = document.getElementById('categoriesList');
        categoriesList.innerHTML = categories.map(category => 
            `<div class="list-group-item">
                <strong>${category.name}</strong>
                ${category.description ? `<br><small class="text-muted">${category.description}</small>` : ''}
                <small class="text-muted">(ID: ${category.id})</small>
            </div>`
        ).join('');
    } catch (error) {
        console.error('Failed to load categories:', error);
    }
}

async function loadCategoriesForProduct() {
    try {
        const categories = await apiCall('/categories');
        const productCategory = document.getElementById('productCategory');
        productCategory.innerHTML = '<option value="">Zgjidh kategorinë</option>' +
            categories.map(category => 
                `<option value="${category.id}">${category.name}</option>`
            ).join('');
    } catch (error) {
        console.error('Failed to load categories for product:', error);
    }
}

// Products functions
document.getElementById('productForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const categoryId = document.getElementById('productCategory').value;
    const supplierId = document.getElementById('productSupplier').value;
    const productData = {
        name: document.getElementById('productName').value,
        price: parseFloat(document.getElementById('productPrice').value),
        description: document.getElementById('productDescription').value,
        stockQuantity: parseInt(document.getElementById('productStock').value) || 0,
        status: document.getElementById('productStatus').value,
        imageUrl: document.getElementById('productImageUrl').value,
        categoryId: categoryId ? parseInt(categoryId) : null,
        supplierId: supplierId ? parseInt(supplierId) : null
    };
    
    try {
        await apiCall('/products', 'POST', productData);
        showMessage('productMessage', 'Produkti u shtua me sukses!', 'success');
        clearForm('productForm');
        loadProducts();
    } catch (error) {
        showMessage('productMessage', 'Gabim: ' + error.message, 'danger');
    }
});

async function loadProducts() {
    try {
        const products = await apiCall('/products');
        const productsList = document.getElementById('productsList');
        productsList.innerHTML = products.map(product => 
            `<div class="list-group-item">
                <strong>${product.name}</strong>
                <br><small class="text-success">€${product.price}</small>
                <br><small class="text-muted">Stok: ${product.stockQuantity} | Status: ${product.status}</small>
                <small class="text-muted">(ID: ${product.id})</small>
            </div>`
        ).join('');
    } catch (error) {
        console.error('Failed to load products:', error);
    }
}

// Customer products view
async function loadCustomerProducts() {
    try {
        const products = await apiCall('/products');
        const customerProducts = document.getElementById('customerProducts');
        
        if (products.length === 0) {
            customerProducts.innerHTML = '<div class="col-12"><p class="text-muted">Nuk ka produkte të disponueshme.</p></div>';
            return;
        }
        
        customerProducts.innerHTML = products.map(product => {
            const imageUrl = product.imageUrl || 'https://picsum.photos/seed/' + product.id + '/300/200.jpg';
            const isOutOfStock = product.stockQuantity <= 0;
            
            return `
                <div class="col-md-4 mb-4">
                    <div class="card h-100">
                        <img src="${imageUrl}" class="card-img-top" alt="${product.name}" style="height: 200px; object-fit: cover;">
                        <div class="card-body d-flex flex-column">
                            <h5 class="card-title">${product.name}</h5>
                            <p class="card-text flex-grow-1">${product.description || 'Produkt i kualitetit të lartë'}</p>
                            <div class="d-flex justify-content-between align-items-center">
                                <span class="h4 text-success mb-0">€${product.price}</span>
                                <span class="badge ${isOutOfStock ? 'bg-danger' : 'bg-success'}">
                                    ${isOutOfStock ? 'Out of Stock' : 'In Stock'}
                                </span>
                            </div>
                            <button class="btn btn-primary mt-2" ${isOutOfStock ? 'disabled' : ''}>
                                <i class="fas fa-shopping-cart"></i> Shto në Shportë
                            </button>
                        </div>
                    </div>
                </div>
            `;
        }).join('');
    } catch (error) {
        console.error('Failed to load customer products:', error);
        document.getElementById('customerProducts').innerHTML = 
            '<div class="col-12"><p class="text-danger">Gabim në ngarkimin e produkteve.</p></div>';
    }
}

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    checkAuth();
});

// Refresh data every 30 seconds for admin
setInterval(() => {
    if (isLoggedIn) {
        loadCities();
        loadSuppliers();
        loadCategories();
        loadProducts();
    }
}, 30000);
