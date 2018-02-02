export interface Meta {
    key: string;
    props: Prop[];
}

export interface Prop {
    name: string;
    label: string;
    ignore?: string[];
    labels?: string[];
    values?: object;
    type?: string;
    valueKey?: string;
}

export interface Page {
    service?: string;
    type: string;
    search?: Prop[];
    ops?: Operate[];
    toolbar?: Operate[];
}

export interface Operate {
    type: string;
    label: string;
    service?: string;
    success?: string;
    parameter?: object;
}