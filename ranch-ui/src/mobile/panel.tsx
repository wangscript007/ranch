import * as React from 'react';
import message from '../util/message';
import Icon from '../ui/icon';
import { service } from './service';
import './i18n';
import './panel.less';

export default class Page<T extends Object={}, E extends object={}> extends React.Component<T, E> {
    render(): JSX.Element {
        let title = message.get(this.getTitle());
        document.title = title;

        return (
            <div id="ranch-ui-mobile">
                <div className="layout-top">
                    <div className="back" onClick={this.back}><Icon code="\ue60c" />{message.get('back')}</div>
                    <div className="title">{title}</div>
                </div>
                <div className="layout-content">{this.getContent()}</div>
            </div>
        );
    }

    protected back(): void {
        service.back();
    }

    protected getTitle(): string {
        return 'title';
    }

    protected getContent(): JSX.Element | JSX.Element[] | null {
        return null;
    }
}