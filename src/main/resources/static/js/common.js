/**
 * ============================================================
 * 公共 JS 工具库 - Blog Admin
 * ============================================================
 * 提供 API 封装、登录态管理、界面工具函数。
 */

// ===================== 登录态管理 =====================

/**
 * 获取当前登录用户信息
 * 从 sessionStorage 读取，登录时存入，退出时清除
 */
function getCurrentUser() {
    const raw = sessionStorage.getItem('currentUser');
    return raw ? JSON.parse(raw) : null;
}

/**
 * 检查是否已登录，未登录则跳转到登录页
 */
function checkAuth() {
    const user = getCurrentUser();
    if (!user) {
        window.location.href = '/login.html';
        return null;
    }
    return user;
}

/**
 * 退出登录：清除用户信息，跳转到登录页
 */
function logout() {
    sessionStorage.removeItem('currentUser');
    window.location.href = '/login.html';
}

// ===================== API 封装 =====================

const API_BASE = '';

/**
 * 通用请求函数，自动处理 JSON 序列化和错误
 *
 * @param {string} method  HTTP 方法
 * @param {string} path    请求路径（如 /api/articles）
 * @param {object} [body]  请求体（可选）
 * @returns {Promise<object>} 解析后的 JSON 响应
 */
async function apiRequest(method, path, body) {
    const options = {
        method,
        headers: { 'Content-Type': 'application/json' },
    };
    if (body !== undefined) {
        options.body = JSON.stringify(body);
    }
    const res = await fetch(API_BASE + path, options);
    return res.json();
}

/** GET 请求快捷方式 */
function apiGet(path) {
    return apiRequest('GET', path);
}

/** POST 请求快捷方式 */
function apiPost(path, body) {
    return apiRequest('POST', path, body);
}

/** PUT 请求快捷方式 */
function apiPut(path, body) {
    return apiRequest('PUT', path, body);
}

/** DELETE 请求快捷方式 */
function apiDelete(path) {
    return apiRequest('DELETE', path);
}

// ===================== Toast 通知 =====================

/**
 * 在页面右上角显示通知
 *
 * @param {string} msg     通知内容
 * @param {string} [type]  类型：'success' | 'error' | 'info'（默认）
 */
function showToast(msg, type) {
    let container = document.querySelector('.toast-container');
    if (!container) {
        container = document.createElement('div');
        container.className = 'toast-container';
        document.body.appendChild(container);
    }

    const item = document.createElement('div');
    item.className = 'toast-item' + (type ? ' ' + type : '');
    item.textContent = msg;
    container.appendChild(item);

    // 触发入场动画
    requestAnimationFrame(() => item.classList.add('show'));

    // 3秒后移除
    setTimeout(() => {
        item.classList.remove('show');
        setTimeout(() => item.remove(), 300);
    }, 3000);
}

// ===================== 弹窗 (Modal) =====================

/**
 * 显示一个 Modal 弹窗
 *
 * @param {string} title    弹窗标题
 * @param {string} bodyHtml 弹窗主体 HTML
 * @param {Array<{text:string, type:string, onClick:function}>} [buttons] 底部按钮
 */
function showModal(title, bodyHtml, buttons) {
    // 移除已有弹窗
    const old = document.querySelector('.modal-overlay');
    if (old) old.remove();

    const overlay = document.createElement('div');
    overlay.className = 'modal-overlay show';

    const defaultButtons = [
        { text: '关闭', type: '', onClick: hideModal }
    ];

    const btns = buttons || defaultButtons;

    overlay.innerHTML = `
        <div class="modal">
            <div class="modal-header">
                <h3>${title}</h3>
                <button class="modal-close" onclick="hideModal()">&times;</button>
            </div>
            <div class="modal-body">${bodyHtml}</div>
            <div class="modal-footer">
                ${btns.map((b, i) => `
                    <button class="btn ${b.type ? 'btn-' + b.type : ''}" data-idx="${i}">${b.text}</button>
                `).join('')}
            </div>
        </div>
    `;

    document.body.appendChild(overlay);

    // 绑定按钮事件
    overlay.querySelectorAll('.modal-footer .btn').forEach(btn => {
        const idx = parseInt(btn.dataset.idx);
        btn.addEventListener('click', () => btns[idx].onClick());
    });

    // 点击遮罩关闭
    overlay.addEventListener('click', (e) => {
        if (e.target === overlay) hideModal();
    });
}

/**
 * 关闭弹窗
 */
function hideModal() {
    const overlay = document.querySelector('.modal-overlay');
    if (overlay) overlay.remove();
}

// ===================== 渲染侧边栏 =====================

/**
 * 渲染后台管理页面的侧边栏
 * 需要页面有 id="sidebar" 的元素
 */
function renderSidebar() {
    const user = getCurrentUser();
    if (!user) return;

    const sidebar = document.getElementById('sidebar');
    if (!sidebar) return;

    // 获取当前页面名，用于高亮
    const page = window.location.pathname.split('/').pop() || 'dashboard.html';

    sidebar.innerHTML = `
        <div class="logo">Blog<span>Admin</span></div>
        <div class="nav">
            <a href="/dashboard.html"     class="${page === 'dashboard.html' ? 'active' : ''}">
                <span class="icon">📊</span><span class="nav-text">仪表盘</span>
            </a>
            <a href="/articles.html"      class="${page === 'articles.html' ? 'active' : ''}">
                <span class="icon">📝</span><span class="nav-text">文章管理</span>
            </a>
            <a href="/categories.html"    class="${page === 'categories.html' ? 'active' : ''}">
                <span class="icon">📂</span><span class="nav-text">分类管理</span>
            </a>
            <a href="/remote-data.html"   class="${page === 'remote-data.html' ? 'active' : ''}">
                <span class="icon">📡</span><span class="nav-text">远程数据</span>
            </a>
        </div>
        <div class="user-info">
            <span class="username">${escapeHtml(user.nickname || user.username)}</span>
            <button class="logout-btn" onclick="confirmLogout()">退出</button>
        </div>
    `;
}

/** 退出确认 */
function confirmLogout() {
    showModal('确认退出', '<p>确定要退出登录吗？</p>', [
        { text: '取消', type: '', onClick: hideModal },
        { text: '确定', type: 'danger', onClick: () => { hideModal(); logout(); } }
    ]);
}

/**
 * HTML 转义，防止 XSS
 */
function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

/**
 * 格式化日期（后端 LocalDateTime 格式 "yyyy-MM-ddTHH:mm:ss" 转 "yyyy-MM-dd HH:mm:ss"）
 */
function formatDateTime(dt) {
    if (!dt) return '-';
    return dt.replace('T', ' ');
}
