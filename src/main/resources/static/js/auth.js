const API_URL = 'http://localhost:8080/users';

const Auth = {
    login: async (username, password) => {
        try {
            const response = await fetch(`${API_URL}/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, password })
            });

            if (response.ok) {
                const data = await response.json();
                localStorage.setItem('token', data.token);
                localStorage.setItem('username', data.username);
                return { success: true };
            } else {
                return { success: false, message: 'Invalid credentials' };
            }
        } catch (error) {
            return { success: false, message: 'Network error' };
        }
    },

    register: async (username, password) => {
        try {
            const response = await fetch(`${API_URL}/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, password })
            });

            if (response.ok) {
                return { success: true };
            } else {
                const text = await response.text();
                return { success: false, message: text || 'Registration failed' };
            }
        } catch (error) {
            return { success: false, message: 'Network error' };
        }
    },

    logout: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        window.location.href = 'login.html';
    },

    getToken: () => {
        return localStorage.getItem('token');
    },

    getUsername: () => {
        return localStorage.getItem('username');
    },

    requireAuth: () => {
        if (!localStorage.getItem('token')) {
            window.location.href = 'login.html';
        }
    },

    checkLoggedIn: () => {
        if (localStorage.getItem('token')) {
            window.location.href = 'index.html';
        }
    }
};
