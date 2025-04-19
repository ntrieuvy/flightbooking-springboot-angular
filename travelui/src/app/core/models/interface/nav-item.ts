export interface NavItem {
    label: string;
    slug?: string;
    icon?: string;
    children?: NavItem[];
}