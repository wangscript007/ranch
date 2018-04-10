import * as React from 'react';
import { ComponentProps, Component } from '../basic/component';
import './index.less';

declare const AMap: any;

interface Props extends ComponentProps {
    name?: string;
    address?: boolean;
    change?: boolean;
}

interface State {
    value: string;
    address: string;
}

export default class Map extends Component<Props, State> {
    private mapId: string;

    constructor(props: Props) {
        super(props);

        let value: string = this.getDefaultValue();
        this.state = {
            value: value,
            address: value.substring(value.lastIndexOf(',') + 1)
        };
        this.mapId = this.getId("map");
    }

    componentDidMount(): void {
        let options: any = {
            resizeEnable: true,
            zoom: 13
        };
        if (this.state.value.indexOf(',') > -1) {
            let vs: string[] = this.state.value.split(',');
            if (vs.length > 1)
                options.center = [vs[0], vs[1]];
        }
        let map = new AMap.Map(this.mapId, options);
        let marker = new AMap.Marker({
            map: map,
            bubble: true
        });
        if (!this.props.change)
            return;

        AMap.plugin('AMap.Geocoder', () => {
            let geocoder = new AMap.Geocoder();
            map.on('click', (e: any) => {
                marker.setPosition(e.lnglat);
                geocoder.getAddress(e.lnglat, (status: string, result: any) => {
                    if (status === 'complete') {
                        let address: string = result.regeocode.formattedAddress;
                        address = address.substring(address.indexOf(result.regeocode.addressComponent.district));
                        this.setState({
                            value: e.lnglat.lng + ',' + e.lnglat.lat + ',' + address,
                            address: address
                        });
                    }
                });
            });
        });
    }

    render(): JSX.Element[] {
        let elements: JSX.Element[] = [];
        if (this.props.name)
            elements.push(<input type="hidden" name={this.props.name} value={this.state.value} />);
        if (this.props.address)
            elements.push(<div ui-type="map-address">{this.state.address}</div>);
        elements.push(<div id={this.mapId} ui-type="map" />);

        return elements;
    }
}