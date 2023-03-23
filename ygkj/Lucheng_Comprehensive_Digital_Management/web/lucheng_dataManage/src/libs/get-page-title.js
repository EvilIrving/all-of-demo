import defaultSettings from '@/settings'

const title = defaultSettings.title || 'dcy-fast-cloud'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
