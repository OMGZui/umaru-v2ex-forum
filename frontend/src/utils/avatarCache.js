/**
 * 头像缓存工具类
 * 实现用户头像URL的本地缓存，支持24小时过期时间
 */

const CACHE_PREFIX = 'avatar_cache_';
const CACHE_EXPIRY_HOURS = 24;
const CACHE_EXPIRY_MS = CACHE_EXPIRY_HOURS * 60 * 60 * 1000; // 24小时转换为毫秒

/**
 * 缓存数据结构
 * {
 *   url: string,           // 头像URL
 *   timestamp: number,     // 缓存时间戳
 *   expiry: number        // 过期时间戳
 * }
 */

class AvatarCacheUtil {
  /**
   * 生成缓存键名
   * @param {string|number} userId - 用户ID
   * @returns {string} 缓存键名
   */
  static getCacheKey(userId) {
    return `${CACHE_PREFIX}${userId}`;
  }

  /**
   * 检查缓存是否过期
   * @param {Object} cacheData - 缓存数据
   * @returns {boolean} 是否过期
   */
  static isExpired(cacheData) {
    if (!cacheData || !cacheData.expiry) {
      return true;
    }
    return Date.now() > cacheData.expiry;
  }

  /**
   * 获取用户头像缓存
   * @param {string|number} userId - 用户ID
   * @returns {string|null} 缓存的头像URL，如果不存在或已过期则返回null
   */
  static getAvatar(userId) {
    try {
      const cacheKey = this.getCacheKey(userId);
      const cachedData = localStorage.getItem(cacheKey);
      
      if (!cachedData) {
        return null;
      }

      const parsedData = JSON.parse(cachedData);
      
      // 检查是否过期
      if (this.isExpired(parsedData)) {
        // 清除过期缓存
        this.removeAvatar(userId);
        return null;
      }

      return parsedData.url;
    } catch (error) {
      console.warn('Error reading avatar cache:', error);
      return null;
    }
  }

  /**
   * 设置用户头像缓存
   * @param {string|number} userId - 用户ID
   * @param {string} avatarUrl - 头像URL
   * @returns {boolean} 是否设置成功
   */
  static setAvatar(userId, avatarUrl) {
    try {
      if (!userId || !avatarUrl) {
        return false;
      }

      const cacheKey = this.getCacheKey(userId);
      const now = Date.now();
      const cacheData = {
        url: avatarUrl,
        timestamp: now,
        expiry: now + CACHE_EXPIRY_MS
      };

      localStorage.setItem(cacheKey, JSON.stringify(cacheData));
      return true;
    } catch (error) {
      console.warn('Error setting avatar cache:', error);
      return false;
    }
  }

  /**
   * 移除用户头像缓存
   * @param {string|number} userId - 用户ID
   * @returns {boolean} 是否移除成功
   */
  static removeAvatar(userId) {
    try {
      const cacheKey = this.getCacheKey(userId);
      localStorage.removeItem(cacheKey);
      return true;
    } catch (error) {
      console.warn('Error removing avatar cache:', error);
      return false;
    }
  }

  /**
   * 清除所有头像缓存
   * @returns {number} 清除的缓存数量
   */
  static clearAllAvatars() {
    try {
      let count = 0;
      const keys = Object.keys(localStorage);
      
      keys.forEach(key => {
        if (key.startsWith(CACHE_PREFIX)) {
          localStorage.removeItem(key);
          count++;
        }
      });

      return count;
    } catch (error) {
      console.warn('Error clearing avatar cache:', error);
      return 0;
    }
  }

  /**
   * 清除过期的头像缓存
   * @returns {number} 清除的过期缓存数量
   */
  static clearExpiredAvatars() {
    try {
      let count = 0;
      const keys = Object.keys(localStorage);
      
      keys.forEach(key => {
        if (key.startsWith(CACHE_PREFIX)) {
          try {
            const cachedData = JSON.parse(localStorage.getItem(key));
            if (this.isExpired(cachedData)) {
              localStorage.removeItem(key);
              count++;
            }
          } catch (error) {
            // 如果解析失败，也删除这个无效的缓存
            localStorage.removeItem(key);
            count++;
          }
        }
      });

      return count;
    } catch (error) {
      console.warn('Error clearing expired avatar cache:', error);
      return 0;
    }
  }

  /**
   * 获取缓存统计信息
   * @returns {Object} 缓存统计信息
   */
  static getCacheStats() {
    try {
      const keys = Object.keys(localStorage);
      let totalCount = 0;
      let expiredCount = 0;
      let totalSize = 0;

      keys.forEach(key => {
        if (key.startsWith(CACHE_PREFIX)) {
          totalCount++;
          const value = localStorage.getItem(key);
          totalSize += key.length + (value ? value.length : 0);
          
          try {
            const cachedData = JSON.parse(value);
            if (this.isExpired(cachedData)) {
              expiredCount++;
            }
          } catch (error) {
            expiredCount++;
          }
        }
      });

      return {
        totalCount,
        expiredCount,
        validCount: totalCount - expiredCount,
        totalSize,
        expiryHours: CACHE_EXPIRY_HOURS
      };
    } catch (error) {
      console.warn('Error getting cache stats:', error);
      return {
        totalCount: 0,
        expiredCount: 0,
        validCount: 0,
        totalSize: 0,
        expiryHours: CACHE_EXPIRY_HOURS
      };
    }
  }

  /**
   * 批量设置头像缓存
   * @param {Array} avatarList - 头像列表 [{userId, avatarUrl}, ...]
   * @returns {number} 成功设置的数量
   */
  static batchSetAvatars(avatarList) {
    if (!Array.isArray(avatarList)) {
      return 0;
    }

    let successCount = 0;
    avatarList.forEach(({ userId, avatarUrl }) => {
      if (this.setAvatar(userId, avatarUrl)) {
        successCount++;
      }
    });

    return successCount;
  }
}

export default AvatarCacheUtil;
